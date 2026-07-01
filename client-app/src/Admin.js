import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import useCourses from "./hooks/useCourses";
import { fetchAchievementMetrics, createAchievement } from "./api/adminApi";
import { createTask } from "./api/tasksApi";
import ExamCreation from "./components/ExamCreation";
import { createExam, deleteExam, deleteAttempt } from "./api/examsApi";
import { getMyExams, getExamResults } from "./api/adminApi";
import { toast } from "react-toastify";
import "./Admin.css";

const Admin = ({ user }) => {
    const token = user?.token;

    const [adminView, setAdminView] = useState("admin");
    const [achievementForm, setAchievementForm] = useState({ name: "", description: "", metric: "FRIENDS_COUNT", code: "", requirement: "" });
    const [courseForm, setCourseForm] = useState({ name: "" });
    const { courses, addCourse } = useCourses(token);
    const [chosenCourse, setChosenCourse] = useState();
    const [taskForm, setTaskForm] = useState({ curseId: "", name: "", exp: "" });
    const [metrics, setMetrics] = useState();
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

    const handleAchievementChange = createHandleChange(setAchievementForm);
    const handleCourseChange = createHandleChange(setCourseForm);
    const handleCourseSet = createHandleChange(setChosenCourse);
    const handleTaskChange = createHandleChange(setTaskForm);
    const handleExamChange = createHandleChange(setExamForm);

    const handleAddCourse = async () => {
        if (!courseForm.name.trim()) {
            toast.error("Nazwa nie może być pusta!");
            return;
        }
        
        if (!alphaNumericRegex.test(courseForm.name)) {
            toast.error("Nazwa powinna zawierać tylko litery lub cyfry!");
            return;
        }

        try {
            await addCourse(courseForm.name);
            setCourseForm({ name: "" });
            toast.success("Utworzono kurs");
        } catch (err) {
            toast.error(err.message);
        }
    }

    const handleAddAchievement = async () => {
        if (!achievementForm.name.trim()) {
            toast.error("Nazwa nie może być pusta!");
            return;
        }
        
        if (!alphaNumericRegex.test(achievementForm.name)) {
            toast.error("Nazwa powinna zawierać tylko litery lub cyfry!");
            return;
        }

        if (!achievementForm.description.trim()) {
            toast.error("Opis nie może być pusty!");
            return;
        }
        
        if (!alphaNumericRegex.test(achievementForm.description)) {
            toast.error("Opis powinien zawierać tylko litery lub cyfry!");
            return;
        }

        if (!achievementForm.code.trim()) {
            toast.error("Kod nie może być pusty!");
            return;
        }
        
        if (!alphaNumericRegex.test(achievementForm.code)) {
            toast.error("Kod powinien zawierać tylko litery lub cyfry!");
            return;
        }

        if (!achievementForm.requirement.trim()) {
            toast.error("Wymagana liczba nie może być pusta!");
            return;
        }

        if (achievementForm.requirement < 0) {
            toast.error("Wymagana liczba nie może być mniejsza od 0!");
            return;
        }

        try {
            await createAchievement(token, achievementForm);
            setAchievementForm({ name: "", description: "", metric: achievementForm.metric, code: "", requirement: "" });
            toast.success("Utworzono nowe osiągnięcie");
        } catch (err) {
            toast.error(err.message);
        }
    }

    const handleAddTask = async () => {
        if (!taskForm.name.trim()) {
            toast.error("Nazwa nie może być pusta!");
            return;
        }
        
        if (!alphaNumericRegex.test(taskForm.name)) {
            toast.error("Nazwa powinna zawierać tylko litery lub cyfry!");
            return;
        }

        if (!taskForm.courseId) {
            toast.error("Najpierw proszę wybrać kurs!");
            return;
        }
        
        if (!taskForm.exp.trim()) {
            toast.error("Doświadczenie nie może być puste!");
            return;
        }
        
        if (taskForm.exp <= 0) {
            toast.error("Ilość doświadczenia nie może być <=0!");
            return;
        }

        try {
            await createTask(token, taskForm.courseId, taskForm.name, taskForm.exp);
            setTaskForm({ name: "", exp: "", courseId: taskForm.courseId });
            toast.success("Utworzono nowe zadanie");
        } catch (err) {
            toast.error(err.message);
        }
    }

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

    const handleOpenExam = async (examId) => {
        try {
            const examDetails = await getExamResults(token, examId);
            setParticipants(examDetails);
            setShowParticipants(true);
            setOpenedExamId(examId);
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

    const handleDeleteExam = async (examId) => {
        if (!window.confirm("Delete this exam?"))
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

    const handleDeleteAttempt = async (attemptId) => {
        if (!window.confirm("Delete this attempt?"))
            return;
    
        await deleteAttempt(user.token, openedExamId, attemptId);
    
        setParticipants(prev =>
            prev.filter(p => p.examHistoryDTO.attemptId !== attemptId)
        );
    };

    const loadMeta = async () => {
        if (!token) return;

        try {
            const data = await fetchAchievementMetrics(token);
            setMetrics(data);
        } catch (err) {
            console.error("Error fetching metrics:", err);
            toast.error("Error fetching metrics");
        }
    };

    useEffect(() => {
        loadMeta();
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

    return (<>
        {adminView === "admin" &&
            (<div className="admin-page">
                <div className="admin-header">
                    <h2>Panel admina</h2>
                    <Link to="/">Wróć</Link>
                </div>

                <div className="admin-section">
                    <h4>Osiągnięcia</h4>
                    <input name="name" placeholder="Nazwa" value={achievementForm.name} onChange={handleAchievementChange} />
                    <input name="description" placeholder="Opis" value={achievementForm.description} onChange={handleAchievementChange} />
                    <input name="code" placeholder="Kod" value={achievementForm.code} onChange={handleAchievementChange} />
                    <input type="number" min={0} name="requirement" placeholder="Wymaganie" value={achievementForm.requirement} onChange={handleAchievementChange} />
                    <select name="metric" value={achievementForm.metric} onChange={handleAchievementChange}>
                        {metrics?.map(metric => (
                            <option key={metric} value={metric}>
                                {metric}
                            </option>
                        ))}
                    </select>
                    <button onClick={handleAddAchievement}>
                        Dodaj osiągnięcia
                    </button>
                </div>
                <div className="admin-section">
                    <h4>Kursy</h4>
                    <input name="name" placeholder="Nazwa" value={courseForm.name} onChange={handleCourseChange} />
                    <button onClick={handleAddCourse}>
                        Dodaj kurs
                    </button>
                </div>
                <div className="admin-section">
                    <h4>Zadania</h4>
                    <select name="courseId" id={taskForm.courseId} value={taskForm.curseId} onChange={handleTaskChange}>
                        <option value="" disabled>Wybierz kurs</option>

                        {courses.map(course => (
                            <option key={course.id} value={course.id}>
                                {course.name}-{course.id}
                            </option>
                        ))}
                    </select>
                    <input name="name" placeholder="Nazwa" value={taskForm.name} onChange={handleTaskChange} />
                    <input type="number" min={1} name="exp" placeholder="Punkty doświadczenia" value={taskForm.exp} onChange={handleTaskChange} />
                    <button onClick={handleAddTask}>
                        Dodaj zadanie
                    </button>
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
                                <p>Kod: {exam.code}</p>
                                <p>Długość (min): {exam.timeLimit} minutes</p>
                                <button onClick={() => handleDeleteExam(exam.id)}>
                                    Usuń
                                </button>
                                <button onClick={() => handleOpenExam(exam.id)}>
                                    Sprawdź wyniki
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
                            <span>Poprawne</span>
                            <span>Punkty</span>
                            <span>%</span>
                            <span>Ocena</span>
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
                        Close
                    </button>

                </div>
            </div>
        )}
    </>
    );
};

export default Admin;