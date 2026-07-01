import React, { useState } from "react";
import Friends from "./components/Friends.js";
import HomeContent from "./components/HomeContent.js";
import Achievements from "./components/Achievements.js";
import Header from "./components/Header.js";
import Arena from "./components/Arena.js";
import Leaderboard from "./components/Leaderboard.js";
import Customization from "./components/Customization.js";
import ExamPage from "./components/ExamPage.js"
import ExamSession from "./components/ExamSession.js"
import "./Home.css";

export default function Home({ user, setUser }) {
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [activeView, setActiveView] = useState("home");
    const [points, setPoints] = useState(user?.points);
    const [friendToFight, setFriendToFight] = useState();
    const [character, setCharacter] = useState(user?.character);
    const [currentExamAttempt, setCurrentExamAttempt] = useState();
    const [selectedAnswers, setSelectedAnswers] = useState({});
    const [powerUps, setPowersUps] = useState([]);
    const [coins, setCoins] = useState(user?.coins);

    return (
        <div className="page">
            <Header user={user} setUser={setUser} points={points} setActiveView={setActiveView} selectedCourse={selectedCourse} setSelectedCourse={setSelectedCourse} coins={coins}/>

            <main>
                {user ? (
                    <>
                        <div className="main-content">
                            {activeView === "home" && (
                                <HomeContent user={user} selectedCourse={selectedCourse} setSelectedCourse={setSelectedCourse} points={points} setPoints={setPoints} character={character} setCharacter={setCharacter} setActiveView={setActiveView}/>
                            )}
                            {activeView === "achievements" && (
                                <Achievements user={user} setUser={setUser} setActiveView={setActiveView}/>
                            )}
                            {activeView === "arena" && (
                                <Arena user={user} setUser={setUser} character={character} friend={friendToFight} setActiveView={setActiveView} setCoins={setCoins}/>
                            )}
                            {activeView === "leaderboard" && (
                                <Leaderboard user={user} character={character} friend={friendToFight} setActiveView={setActiveView}/>
                            )}
                            {activeView === "customization" && (
                                <Customization user={user} setUser={setUser} setActiveView={setActiveView}/>
                            )}
                            {activeView === "exams" && (
                                <ExamPage user={user} setUser={setUser} setCurrentExamAttempt={setCurrentExamAttempt} setActiveView={setActiveView} powerUps={powerUps} setPowerUps={setPowersUps} coins={coins} setCoins={setCoins}/>
                            )}
                            {activeView === "exam" && (
                                <ExamSession user={user} currentExamAttempt={currentExamAttempt} setActiveView={setActiveView} selectedAnswers={selectedAnswers} setSelectedAnswers={setSelectedAnswers} powerUps={powerUps} setPowerUps={setPowersUps}/>
                            )}
                        </div>
                        <Friends user={user} setActiveView={setActiveView} setFriendToFight={setFriendToFight}/>
                    </>
                ) : (
                    <h2 className="no-login-text">
                        Aby korzystać z aplikacji należy się zalogować
                    </h2>
                )}
            </main>
        </div>
    );
}