import React from "react";
import "./Home.css";

export default function Home({ user, setUser }) {
    const token = user?.token;
    const username = user?.username;

    const isLoggedIn = !!token;

    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem("user");
    };

    return (
        <div className="page">
            <header>
                <div className="logo">LearningApp</div>

                <div className="nav">
                    {isLoggedIn ? (
                        <>
                            {/*<span className="user">Witaj, {username}</span>*/}
                            <button className="logout" onClick={handleLogout}>
                                Wyloguj
                            </button>
                        </>
                    ) : (
                        <>
                            <a href="/login">Zaloguj</a>
                            <a href="/register">Zarejestruj</a>
                        </>
                    )}
                </div>
            </header>

            <main>
                {user ? (
                    <h1>Witaj w aplikacji!</h1>
                ) : (
                    <h1>Aby korzystać z aplikacji należy się zalogować</h1>
                )}
            </main>
        </div>
    );
}
