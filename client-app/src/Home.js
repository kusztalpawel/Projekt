import React, { useState } from "react";
import Friends from "./components/Friends.js";
import HomeContent from "./components/HomeContent.js";
import Achievements from "./components/Achievements.js";
import Header from "./components/Header.js";
import Arena from "./components/Arena.js";
import Leaderboard from "./components/Leaderboard.js";
import Customization from "./components/Customization.js";
import "./Home.css";

export default function Home({ user, setUser }) {
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [activeView, setActiveView] = useState("home");
    const [points, setPoints] = useState(user?.points);
    const [friendToFight, setFriendToFight] = useState();
    const [character, setCharacter] = useState(user?.character);

    return (
        <div className="page">
            <Header user={user} setUser={setUser} points={points} setActiveView={setActiveView} selectedCourse={selectedCourse} setSelectedCourse={setSelectedCourse}/>

            <main>
                {user ? (
                    <div className="main-content">
                        {activeView === "home" && (
                            <HomeContent user={user} selectedCourse={selectedCourse} setSelectedCourse={setSelectedCourse} points={points} setPoints={setPoints} character={character} setCharacter={setCharacter} setActiveView={setActiveView}/>
                        )}
                        {activeView === "achievements" && (
                            <Achievements user={user} setUser={setUser} setActiveView={setActiveView}/>
                        )}
                        {activeView === "arena" && (
                            <Arena user={user} setUser={setUser} character={character} friend={friendToFight} setActiveView={setActiveView}/>
                        )}
                        {activeView === "leaderboard" && (
                            <Leaderboard user={user} character={character} friend={friendToFight} setActiveView={setActiveView}/>
                        )}
                        {activeView === "customization" && (
                            <Customization user={user} setUser={setUser} setActiveView={setActiveView}/>
                        )}
                        <Friends user={user} setUser={setUser} setActiveView={setActiveView} setFriendToFight={setFriendToFight}/>
                    </div>
                ) : (
                    <h2 className="no-login-text">
                        Aby korzystać z aplikacji należy się zalogować
                    </h2>
                )}
            </main>
        </div>
    );
}