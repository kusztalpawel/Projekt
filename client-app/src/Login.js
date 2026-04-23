import React, { useState } from "react";
import { Link } from "react-router";
import { useNavigate } from "react-router";
import "./Login.css";

const Login = ({ setUser }) => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const API_URL = "http://localhost:8080";
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const res = await fetch(`${API_URL}/users/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json", },
                body: JSON.stringify({ username, password }),
            });

            const data = await res.json();

            const userData = {
                username: data.username,
                token: data.token,
            };

            setUser(userData);

            localStorage.setItem("user", JSON.stringify(userData));
            navigate("/");

            if (!res.ok) {
                throw new Error(data.message || "Login failed");
            }

            console.log("Logged in user:", data);
        } catch (err) {
            console.error("Login error:", err);
        }
    };

    return (
        <div className="container">
            <div className="card">

                <div className="left">
                    <h1>Czego się dziś nauczymy?</h1>
                    <p>Zaloguj się na swoje konto, aby kontynuować.</p>
                </div>

                <div className="right">
                    <h2>Logowanie</h2>
                    <p className="subtitle">Zaloguj się na konto</p>

                    <form onSubmit={handleSubmit}>
                        <input
                            type="text"
                            placeholder="Login"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />

                        <input
                            type="password"
                            placeholder="Hasło"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />

                        <div className="options">
                            <label>
                                <input type="checkbox" /> Nie wylogowuj
                            </label>
                        </div>

                        <button type="submit">Zaloguj</button>
                    </form>

                    <p className="signup">
                        Nie masz konta? <Link to="/register">Zarejestruj się</Link>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;