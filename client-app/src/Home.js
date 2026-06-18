import React, { useState, useEffect } from "react";
import useCourses from "./hooks/useCourses.js";
import useTasks from "./hooks/useTasks.js";
import Levels from "./components/Levels.js";
import Friends from "./components/Friends.js";
import Character from "./components/Character.js";
import { fetchAchievements } from "./api/usersApi.js";
import "./Home.css";

export default function Home({ user, setUser }) {
    const username = user?.username;

    const isLoggedIn = !!user?.token;
    const isAdmin = user?.role === "ADMIN";

    const [showCourses, setShowCourses] = useState(false);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [newTask, setNewTask] = useState("");
    const [showCourseModal, setShowCourseModal] = useState(false);
    const [newCourseName, setNewCourseName] = useState("");
    const [friends, setFriends] = useState(user?.friends);
    const [character, setCharacter] = useState(user?.character);
    const [points, setPoints] = useState(user?.points);

    const { courses, templates, addCourse, loadCourses, loadTemplates, enrollCourse } = useCourses(user?.token);
    const { tasks, addTask, toggleTask, deleteTask } = useTasks(user?.token, selectedCourse, setSelectedCourse, setPoints);

    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem("user");
        sessionStorage.removeItem("user");
    };

    const handleAddTask = async () => {
        if (!selectedCourse) {
            alert("Wybierz kurs");
            return;
        }

        if (!newTask.trim()) return;

        await addTask(user?.token, newTask);
        setNewTask("");
    };

    const handleToggleTask = (id) => {
        toggleTask(user, id);
    };

    const handleDeleteTask = (id) => {
        deleteTask(user?.token, id);
    };

    const onCreateCourseButtonClick = async () => {
        if (!newCourseName.trim()) return;

        try {
            await addCourse(newCourseName);
            setNewCourseName("");
            setShowCourseModal(false);
        } catch (err) {
            console.error(err);
        }
    };

    const onCourseChosen = (crs) => {
        setSelectedCourse(crs);
        setShowCourses(false);
    }

    const onTemplateChosen = async (tpt) => {
        try{
            await enrollCourse(tpt.id);
            setShowCourses(false);
        } catch (err){
            console.error(err);
        }
    }

    const loadAchievements = async () => {
            try {
                user.achievements = await fetchAchievements(user?.token);
                console.log("Dziala");
            } catch (err) {
                console.error("Error fetching achievements:", err);
            }
        };

    useEffect(() => {
        loadAchievements();
    }, []);

    console.log(user?.achievements);

    return (<>
        <div className="page">
            <header>
                <div className="logo">LearningApp</div>

                <div className="nav">
                    {isLoggedIn ? (
                        <>
                            <div>
                                {isAdmin && (
                                    <a className="header-link" href="/admin">ADMIN PANEL</a>
                                )}
                            </div>
                            <div className="courses-dropdown"
                                onMouseEnter={ async () => {
                                    setShowCourses(true);
                                    await loadCourses();
                                    await loadTemplates();
                                }}
                                onMouseLeave={() =>
                                    setShowCourses(false)
                                }
                            >Kursy ⮟

                                {showCourses && (
                                    <div className="courses-menu">
                                        {courses.map((c) => (
                                            <button
                                                key={c.id}
                                                onClick={() => {onCourseChosen(c)}}
                                            >
                                                {c.name}
                                            </button>
                                        ))}
                                        {templates.map((t) => (
                                            <button className="template"
                                                key={t.id}
                                                onClick={() => {onTemplateChosen(t)}}
                                            >
                                                {t.name}
                                            </button>
                                        ))}
                                        <button onClick={() => (setShowCourseModal(true))}>
                                            + Dodaj kurs
                                        </button>
                                    </div>
                                )}
                            </div>


                            {selectedCourse ? <span className="user">
                                Jestes w {selectedCourse?.name}
                            </span> : <span></span>}

                            <span className="user">
                                Witaj, {username} | Punkty: {points}
                            </span>

                            <button
                                className="header-link"
                                onClick={handleLogout}
                            >
                                Wyloguj
                            </button>
                        </>
                    ) : (
                        <>
                            <a className="header-link" href="/login">Zaloguj</a>
                            <a className="header-link" href="/register">Zarejestruj</a>
                        </>
                    )}
                </div>
            </header>

            <main>
                {user ? (
                    <>
                        <div className="tasks-container">
                            <h1>Twoje zadania</h1>

                            <div className="task-input">
                                <input
                                    type="text"
                                    placeholder="Dodaj nowe zadanie..."
                                    value={newTask}
                                    onChange={(e) =>
                                        setNewTask(e.target.value)
                                    }
                                />

                                <button onClick={handleAddTask}>
                                    Dodaj
                                </button>
                            </div>

                            <ul className="task-list">
                                {tasks.map((task) => (
                                    <li key={task.id} className="task">
                                        <div className="task-left">
                                            <input
                                                type="checkbox"
                                                className="task-checkbox"
                                                checked={task.isDone}
                                                onChange={() => handleToggleTask(task.id)}
                                            />
                                            <span className="task-name">
                                                {task.name}
                                            </span>
                                            <span className="task-points">
                                                {task.points} EXP
                                            </span>
                                        </div>

                                        <button
                                            className="delete-btn"
                                            onClick={() =>
                                                handleDeleteTask(task.id)
                                            }
                                        >
                                            X
                                        </button>
                                    </li>
                                ))}
                            </ul>
                        </div>
                        <Levels currentLevel={selectedCourse?.level ?? 0} currentExperience={selectedCourse?.experience ?? 0} experienceNeeded={selectedCourse?.experienceNeeded ?? 1000000000}/>
                        <Character token = {user?.token} character={character} setCharacter={setCharacter} points = {points} setPoints = {setPoints}/>
                        <Friends token = {user?.token} friends = {friends} setFriends = {setFriends}/>
                    </>
                ) : (
                    <h2>
                        Aby korzystać z aplikacji należy
                        się zalogować
                    </h2>
                )}
            </main>
        </div>
        {showCourseModal && (
            <div className="modal-overlay">
                <div className="modal">

                    <h2>Nowy kurs</h2>

                    <input
                        type="text"
                        placeholder="Nazwa kursu"
                        value={newCourseName}
                        onChange={(e) =>
                            setNewCourseName(e.target.value)
                        }
                    />

                    <div className="modal-buttons">

                        <button onClick={onCreateCourseButtonClick}>
                            Dodaj
                        </button>

                        <button
                            onClick={() => {
                                setShowCourseModal(false);
                                setNewCourseName("");
                            }}
                        >
                            Anuluj
                        </button>

                    </div>
                </div>
            </div>
        )}
    </>
    );
}