import React, { useState } from "react";
import { Link, useNavigate } from "react-router";
import "./Login.css";
import { fetchUserLogin } from "./api/usersApi";
import { jwtDecode } from "jwt-decode";

const Login = ({ setUser }) => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [rememberMe, setRememberMe] = useState(false);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const data = await fetchUserLogin(username, password);
            const decoded = jwtDecode(data.token);

            const userData = {
                username: data.username,
                token: data.token,
                points: data.points,
                achievements: data.achievements,
                friends: data.friends,
                character: data.character,
                role: decoded.role,
            };

            setUser(userData);

            if (rememberMe) {
                localStorage.setItem("user", JSON.stringify(userData));
                sessionStorage.removeItem("user");
            } else {
                sessionStorage.setItem("user", JSON.stringify(userData));
                localStorage.removeItem("user");
            }
            navigate("/");
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
                                <input type="checkbox" checked={rememberMe} onChange={(e) => setRememberMe(e.target.checked)}/> Nie wylogowuj
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