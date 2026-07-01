import React, { useEffect, useState } from "react";
import { fetchTestQuestions, submitExam } from "../api/examsApi";
import { applyPowerUp, fetchPowersUsedInAttempt } from "../api/examsApi";
import "./ExamSession.css";

const ExamSession = ({ user, currentExamAttempt, setActiveView, selectedAnswers, setSelectedAnswers, powerUps, setPowerUps }) => {
    const [questions, setQuestions] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [examResult, setExamResult] = useState(null);
    const [powerUpsUsed, setPowerUpsUsed] = useState({BONUS_TIME: currentExamAttempt.bonusMinutesUsed, BONUS_POINTS: currentExamAttempt.bonusPointsUsed, REMOVE_OPTION: currentExamAttempt.removeOptionUsed});
    const [examFinished, setExamFinished] = useState(false);

    const question = questions[currentIndex];

    const bonusTimePower = powerUps.find(power => power.type === "BONUS_TIME");
    const bonuseMinutes = powerUpsUsed.BONUS_TIME ? bonusTimePower?.value : 0;
    const endTime = new Date(currentExamAttempt.startedAt).getTime() + (currentExamAttempt.timeLimit + bonuseMinutes) * 60 * 1000;

    const calculateRemainingSeconds = () => {
        return Math.max(0, Math.floor((endTime - Date.now()) / 1000));
    };

    const [timeLeft, setTimeLeft] = useState(calculateRemainingSeconds);

    const formatTime = (seconds) => {
        const minutes = Math.floor(seconds / 60);
        const secs = seconds % 60;

        return `${minutes}:${secs.toString().padStart(2, "0")}`;
    };
        
    useEffect(() => {
        if((Date.now() - new Date(currentExamAttempt.startedAt).getTime()) < 5000){
            setSelectedAnswers({});
        }
    }, [setSelectedAnswers]);

    useEffect(() => {
        if (examFinished) return;

        if (timeLeft <= 0) {
            handleSubmitAnswers();
            return;
        }
        const interval = setInterval(() => {setTimeLeft(calculateRemainingSeconds());}, 1000);
        
        return () => clearInterval(interval);
    }, [timeLeft]);

    useEffect(() => {
        const loadQuestions = async () => {
            try {
                const data = await fetchTestQuestions(user.token, currentExamAttempt.attemptId);
                setQuestions(data);
            } catch (err) {
                console.error(err);
            }
        };

        loadQuestions();
    }, [currentExamAttempt.examId, user.token]);
    
    const handleSelectAnswer = (questionId, answerId) => {
        setSelectedAnswers(prev => ({
            ...prev,
            [questionId]: answerId
        }));
    };

    const selectedAnswersSend = Object.entries(selectedAnswers).map(([questionId, answerId]) => ({
        questionId: Number(questionId),
        answerId,
    }));

    const handleSubmitAnswers = async () => {
        try {
            const examRes = await submitExam(user?.token, currentExamAttempt.attemptId, selectedAnswersSend);
            setExamResult(examRes);
            setExamFinished(true);
        } catch (err) {
            console.error(err);
        }
    }

    const handleUsePower = async (powerId) => {
        try {
            const update = await applyPowerUp(user?.token, currentExamAttempt.attemptId, powerId);
            setPowerUps(update);
            const usage = await fetchPowersUsedInAttempt(user?.token, currentExamAttempt.attemptId);
            setPowerUpsUsed({BONUS_TIME: usage.bonusMinutesUsed, BONUS_POINTS: usage.bonusPointsUsed, REMOVE_OPTION: usage.removeOptionUsed});
            const updatedQuestions = await fetchTestQuestions(user.token, currentExamAttempt.attemptId);
            setQuestions(updatedQuestions);
        } catch (err) {
            console.error(err);
        }
    }

    return (
        <div className="exam-session">
            <div className="exam-powers">
                <h3>Power Ups</h3>

                {powerUps.map(power => (
                    <button
                        key={power.id}
                        onClick={() => handleUsePower(power.id)}
                        disabled={power.ownedAmount === 0 || powerUpsUsed[power.type]}
                    >
                        {power.name} ({power.ownedAmount})
                    </button>
                ))}
            </div>
            <div className="exam-container">
                <div className="exam-header">
                    <div className="exam-title">
                        <h1>{currentExamAttempt.name}</h1>
                    </div>

                    <div className="exam-timer">
                        {formatTime(timeLeft)}
                    </div>
                </div>

                <div className="question-progress">
                    {questions.map((q, index) => (
                        <button
                            key={q.questionId}
                            className={`question-button ${currentIndex === index ? "active" : ""} ${selectedAnswers[q.questionId] ? "answered" : ""}`}
                            onClick={() => setCurrentIndex(index)}
                        >
                            {index + 1}
                        </button>
                    ))}
                </div>

                <div className="exam-counter">
                    Question {currentIndex + 1} / {questions.length}
                </div>

                <div className="question-card">
                    <div className="question-text">
                        {question?.text}
                    </div>

                    <div className="answers">
                        {Object.entries(question?.answers ?? {}).map(([answerId, answerText]) => (
                            <label key={answerId} className="answer-option">
                                <input
                                    type="radio"
                                    name={`question-${question?.questionId}`}
                                    checked={selectedAnswers[question?.questionId] === Number(answerId)}
                                    onChange={() =>
                                        handleSelectAnswer(question?.questionId, Number(answerId))
                                    }
                                />
                                {answerText}
                            </label>

                        ))}
                    </div>

                </div>
            
                <div className="exam-footer">
                    <button className="previous-button"
                        disabled={currentIndex === 0}
                        onClick={() => setCurrentIndex(prev => prev - 1)}
                    >
                        Previous
                    </button>
                    <button className="next-button"
                        disabled={currentIndex >= questions.length - 1}
                        onClick={() => setCurrentIndex(prev => prev + 1)}
                    >
                        Next
                    </button>

                    <button className="submit-button" onClick={() => handleSubmitAnswers()}>
                        Submit
                    </button>
                </div>
            </div>
            {examResult && (
                <div className="exam-result-overlay">

                    <div className="exam-result-modal">

                        <h1>Egzamin zakończony</h1>

                        <div className="exam-result-stat">
                            Poprawne odpowiedzi:
                            <span>
                                {examResult.correctAnswers} / {examResult.totalQuestions}
                            </span>
                        </div>

                        <div className="exam-result-stat">
                            Zdobyte punkty:
                            <span>
                                {examResult.points} / {examResult.totalPoints}
                            </span>
                        </div>

                        <div className="exam-result-stat">
                            Wynik:
                            <span>
                                {examResult.percentage.toFixed(2)}%
                            </span>
                        </div>

                        <div className="exam-grade">
                            Ocena: {examResult.grade.toFixed(1)}
                        </div>

                        <button
                            className="exam-result-button"
                            onClick={() => setActiveView("home")}
                        >
                            Strona główna
                        </button>

                    </div>

                </div>
            )}
        </div>);
};

export default ExamSession;