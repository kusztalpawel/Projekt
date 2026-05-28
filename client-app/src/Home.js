import React, { useState } from "react";
import useCourses from "./hooks/useCourses.js";
import useTasks from "./hooks/useTasks.js";
import "./Home.css";

export default function Home({ user, setUser }) {
    const username = user?.username;

    const isLoggedIn = !!user?.token;

    const [showCourses, setShowCourses] = useState(false);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [newTask, setNewTask] = useState("");
    const [showCourseModal, setShowCourseModal] = useState(false);
    const [newCourseName, setNewCourseName] = useState("");

    const { courses, setCourses, addCourse } = useCourses(user?.token);
    const { tasks, addTask, toggleTask, deleteTask } = useTasks(user?.token, selectedCourse, setSelectedCourse);

    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem("user");
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
        toggleTask(user?.token, id);
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

    return (<>
        <div className="page">
            <header>
                <div className="logo">LearningApp</div>

                <div className="nav">
                    {isLoggedIn ? (
                        <>
                            <div className="courses-dropdown">
                                <button
                                    className="courses-btn"
                                    onClick={() =>
                                        setShowCourses(!showCourses)
                                    }
                                >
                                    Kursy ⮟
                                </button>

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
                                        <button onClick={() => (setShowCourseModal(true))}>
                                            + Dodaj kurs
                                        </button>
                                    </div>
                                )}
                            </div>


                            <span className="user">
                                Jestes w {selectedCourse?.name} | LVL {selectedCourse?.level ?? 0} | EXP {selectedCourse?.experience ?? 0} |
                            </span>

                            <span className="user">
                                Witaj, {username}
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
                                            checked={task.done}
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
                ) : (
                    <h1>
                        Aby korzystać z aplikacji należy
                        się zalogować
                    </h1>
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