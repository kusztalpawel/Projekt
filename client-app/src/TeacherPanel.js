import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import ExamCreation from "./components/ExamCreation";
import { createExam, deleteExam, deleteAttempt } from "./api/examsApi";
import { getMyExams, getExamResults } from "./api/adminApi";
import { toast } from "react-toastify";
import "./TeacherPanel.css";

const TeacherPanel = ({ user }) => {
    const token = user?.token;

    const [adminView, setAdminView] = useState("admin");
    const [examForm, setExamForm] = useState({ name: "", code: "", timeLimit: "" });
    const [currentExam, setCurrentExam] = useState(null);
    const [createdExams, setCreatedExams] = useState(null);
    const [participants, setParticipants] = useState([]);
    const [showParticipants, setShowParticipants] = useState(false);
    const [openedExamId, setOpenedExamId] = useState();
    const alphaNumericRegex = /^[A-Za-z0-9]+$/;

    const createHandleChange = (setState) => (e) => {
        setState(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const handleExamChange = createHandleChange(setExamForm);

    const handleCreateExam = async () => {
        if (!examForm.name.trim()) {
            toast.error("Nazwa nie może być pusta!");
            return;
        }

        if (!alphaNumericRegex.test(examForm.name)) {
            toast.error("Nazwa powinna zawierać tylko litery lub cyfry!");
            return;
        }

        if (examForm.code.length !== 6) {
            toast.error("Kod musi zawierać 6 cyfr!");
            return;
        }

        if (!examForm.timeLimit.trim()) {
            toast.error("Długość egzaminu nie może być pusta!");
            return;
        }

        if (examForm.timeLimit <= 0) {
            toast.error("Długość egzaminu nie być <=0!");
            return;
        }

        try {
            const createdExam = await createExam(token, examForm);
            setCurrentExam(createdExam);
            setExamForm({ name: "", code: "", timeLimit: "" });
            setAdminView("createExam");
        } catch (err) {
            toast.error(err.message);
        }
    };

    const handleDeleteExam = async (examId) => {
        if (!window.confirm("Czy na pewno chcesz usunąć ten egzamin?"))
            return;
        try {
            await deleteExam(user.token, examId);

            setCreatedExams(prev =>
                prev.filter(exam => exam.id !== examId)
            );

        } catch (err) {
            console.error(err);
        }
    };

    const handleOpenExam = async (examId) => {
        try {
            const examDetails = await getExamResults(token, examId);
            setOpenedExamId(examId);
            setParticipants(examDetails);
            setShowParticipants(true);
        } catch (err) {
            console.error(err);
        }
    };

    const handleLoadExams = async () => {
        try {
            const myExams = await getMyExams(token);
            setCreatedExams(myExams);
        } catch (err) {
            console.error(err);
        }
    }

    useEffect(() => {
        handleLoadExams();
    }, []);

    useEffect(() => {
        if(showParticipants){
            document.body.style.overflow = 'hidden'
        }
            
        else{
            document.body.style.overflow = 'auto'
        } 
    }, [showParticipants]);

    const handleDeleteAttempt = async (attemptId) => {
        if (!window.confirm("Czy na pewno chcesz usunąć to podejście?"))
            return;

        await deleteAttempt(user.token, openedExamId, attemptId);

        setParticipants(prev =>
            prev.filter(p => p.examHistoryDTO.attemptId !== attemptId)
        );
    };

    return (<>
        {adminView === "admin" &&
            (<div className="admin-page">
                <div className="admin-header">
                    <h2>Panel nauczyciela</h2>
                    <Link to="/">Powrót</Link>
                </div>

                <div className="admin-section">
                    <h4>Egzaminy</h4>
                    <input name="name" placeholder="Nazwa egzaminu" value={examForm.name} onChange={handleExamChange} />
                    <input name="code" maxLength={6} placeholder="6 cyfrowy kod" value={examForm.code.replace(/\D/g, "")} onChange={handleExamChange} />
                    <input type="number" min={1} name="timeLimit" placeholder="Długośc egzaminu (minuty)" value={examForm.timeLimit} onChange={handleExamChange} />
                    <button onClick={handleCreateExam}>
                        Utwórz egzamin
                    </button>
                </div>
                <hr />
                <div className="admin-section">
                    <h2>Twoje egzaminy</h2>
                    <div className="created-exams">
                        {createdExams?.map((exam) => (
                            <div className="exam-card" key={exam.id}>
                                <h3>{exam.name}</h3>
                                <p>Kod dostępu: {exam.code}</p>
                                <p>Długość: {exam.timeLimit} min</p>
                                <button onClick={() => handleDeleteExam(exam.id)}>
                                    Usuń
                                </button>
                                <button onClick={() => handleOpenExam(exam.id)}>
                                    Zobacz wyniki
                                </button>
                            </div>
                        ))}
                    </div>
                </div>

            </div>)}
        {adminView === "createExam" && currentExam && (<ExamCreation user={user} currentExam={currentExam} setAdminView={setAdminView} handleLoadExams={handleLoadExams}/>)}
        {showParticipants && (
            <div className="participants-result-overlay">
                <div className="participants-modal">

                    <h2>Wyniki egzaminu</h2>

                    <div className="participants-table">

                        <div className="participants-header">
                            <span>Student</span>
                            <span>Poprawne odpowiedzi</span>
                            <span>Punkty</span>
                            <span>%</span>
                            <span>Ocena</span>
                            <span> </span>
                        </div>

                        {participants.map((participant, index) => (
                            <div
                                key={index}
                                className="participants-row"
                            >
                                <span>
                                    {participant.username}
                                </span>

                                <span>
                                    {participant.examHistoryDTO.correctAnswers}/
                                    {participant.examHistoryDTO.totalQuestions}
                                </span>

                                <span>
                                    {participant.examHistoryDTO.points}/
                                    {participant.examHistoryDTO.totalPoints}
                                </span>

                                <span>
                                    {participant.examHistoryDTO.percentage.toFixed(1)}%
                                </span>

                                <span className="participant-grade">
                                    {participant.examHistoryDTO.grade.toFixed(1)}
                                </span>
                                <button onClick={() => handleDeleteAttempt(participant.examHistoryDTO.attemptId)}>
                                    Usuń
                                </button>
                            </div>
                        ))}

                    </div>

                    <button
                        onClick={() => setShowParticipants(false)}
                    >
                        Zamknij
                    </button>

                </div>
            </div>
        )}
    </>
    );
};

export default TeacherPanel;