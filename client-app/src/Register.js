import React, { useState } from "react";
import { Link } from "react-router";
import "./Login.css";

const Register = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const API_URL = "http://localhost:8080";

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            alert("Passwords do not match");
            return;
        }

        try {
            const res = await fetch(`${API_URL}/users/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, password }),
            });

            const data = await res.json();

            if (!res.ok) {
                throw new Error(data.message || "Register failed");
            }

            console.log("Registered user:", data);
        } catch (err) {
            console.error("Register error:", err);
        }
    };

    return (
        <div className="container">
            <div className="card">

                <div className="left">
                    <h1>Utwórz konto</h1>
                    <p>Zarejestruj się, aby rozpocząć przygodę z nauką.</p>
                </div>

                <div className="right">
                    <h2>Rejestracja</h2>
                    <p className="subtitle">Utwórz konto</p>

                    <form onSubmit={handleSubmit}>
                        <input
                            type="text"
                            placeholder="Nazwa użytkownika"
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

                        <input
                            type="password"
                            placeholder="Potwierdź hasło"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />

                        <button type="submit">Zarejestruj</button>
                    </form>
                    <p className="signup">
                        Masz już konto? <Link to="/login">Zaloguj się</Link>
                    </p>
                </div>

            </div>
        </div>
    );
};

export default Register;