import React, { useState, useEffect } from "react";
import { joinExam, fetchPowerUps, buyPowerUp } from "../api/examsApi";
import { getExamHistory, fetchUserProgress } from "../api/usersApi";
import { useNavigate } from "react-router-dom";
import { GiTwoCoins } from "react-icons/gi";

import "./ExamPage.css";

const ExamPage = ({ user, setCurrentExamAttempt, setActiveView, powerUps, setPowerUps, coins, setCoins }) => {
    const isTeacher = user?.role === "TEACHER";
    const [code, setCode] = useState("");
    const [examHistory, setExamHistory] = useState([]);

    useEffect(() => {
        const loadHistory = async () => {
            try {
                const history = await getExamHistory(user.token);
                setExamHistory(history);
            } catch (err) {
                console.error(err.message);
            }
        };
        loadHistory();
    }, [user.token]);

    useEffect(() => {
        const loadPowers = async () => {
            try {
                const powers = await fetchPowerUps(user?.token);
                setPowerUps(powers);
            } catch (err) {
                console.error(err);
            }
        };

        loadPowers();
        handleupdateCoins();
    }, [])

    const handleBuy = async (powerUpId) => {
        try {
            const update = await buyPowerUp(user?.token, powerUpId);
            setPowerUps(update);
            handleupdateCoins();
        } catch (err) {
            console.error(err);
        }
    };

    const handleStart = async () => {
        try {
            const exam = await joinExam(user.token, code);
            setCurrentExamAttempt(exam);
            setActiveView("exam");
        } catch (err) {
            alert(err.message);
        }
    };

    const handleupdateCoins = async () => {
            try {
                const update = await fetchUserProgress(user?.token);
                setCoins(update.powerCoins);
            } catch (error) {
                console.error(error);
            }
        };

    const navigate = useNavigate();

    return (
        <div className="exam-page">
            <div className="exam-layout">
                <div className="power-shop">
                    <h2>Sklep bonusów</h2>
                    <div className="coin-counter">
                        <GiTwoCoins size={"1.2em"}/> {coins}
                    </div>
                    {powerUps.map(power => (
                        <div className="power-card" key={power.id}>
                            <h3>{power.name}</h3>
                            <p>{power.description}</p>
                            <p>Cena: {power.price} żetony</p>
                            <p>Posiadane: {power.ownedAmount}</p>
                            <button onClick={() => handleBuy(power.id)}>
                                Kup
                            </button>
                        </div>
                    ))}
                </div>
                <div className="exam-content">
                    <h2>Rozpocznij egzamin</h2>
                    <input
                        type="text"
                        name="Code"
                        maxLength={6}
                        placeholder="Podaj kod egzaminu"
                        value={code}
                        onChange={(e) => setCode(e.target.value.replace(/\D/g, ""))}
                        required
                    />
                    <div className="exam-buttons">
                        <button onClick={handleStart}>Start</button>
                        <button onClick={() => setActiveView("home")}>Back</button>
                    </div>
                    {isTeacher && (
                        <div>
                            <button onClick={() => { navigate("/teacher") }}>Panel nauczyciela</button>
                        </div>
                    )}
                    <div className="exam-history">
                        <h2>Twoje wyniki z egzaminów</h2>
                        {examHistory.length === 0 ? (
                            <p>Brak</p>
                        ) : (
                            examHistory.map((exam, index) => (
                                <div
                                    className="exam-history-card"
                                    key={index}
                                >
                                    <div className="exam-history-left">
                                        <h3>{exam.examName}</h3>
                                        <span>
                                            {new Date(exam.finishedAt).toLocaleString()}
                                        </span>
                                    </div>
                                    <div className="exam-history-right">
                                        <div>
                                            Ocena
                                            <strong>{exam.grade.toFixed(1)}</strong>
                                        </div>
                                        <div>
                                            Punkty
                                            <strong>{exam.points}/{exam.totalPoints}</strong>
                                        </div>
                                        <div>
                                            Wynik
                                            <strong>{exam.percentage.toFixed(1)}%</strong>
                                        </div>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ExamPage;