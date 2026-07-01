import { useState } from "react";
import { Link } from "react-router-dom";
import useCourses from "../hooks/useCourses";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "./Header.css"

export default function Header({ user, setUser, points, setActiveView, selectedCourse, setSelectedCourse}) {
    const isLoggedIn = !!user?.token;
    const isAdmin = user?.role === "ADMIN";
    const isTeacher = user?.role === "TEACHER";
    
    const [showCourses, setShowCourses] = useState(false);
    const [showCourseModal, setShowCourseModal] = useState(false);
    const [newCourseName, setNewCourseName] = useState("");
    const { courses, templates, addCourse, loadCourses, loadTemplates, enrollCourse } = useCourses(user?.token);
    const alphaNumericRegex = /^[A-Za-z0-9]+$/;

    const handleLogout = () => {
        setUser(null);
        localStorage.removeItem("user");
        sessionStorage.removeItem("user");
    };

    const onCreateCourseButtonClick = async () => {
        if (!newCourseName.trim()) {
            toast.error("Nazwa kursu nie może być pusta!");
            return;
        }

        if (!alphaNumericRegex.test(newCourseName)) {
            toast.error("Nazwa kursu powinna zawierać tylko litery lub cyfry.");
            return;
        }

        try {
            await addCourse(newCourseName);
            setNewCourseName("");
            setShowCourseModal(false);
            toast.success("Dodano kurs");
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

    const navigate = useNavigate();

    return (
        <>
            <header className="header-class">
                <div className="logo">LearningApp</div>

                <div className="nav">
                    {isLoggedIn ? (
                        <>
                            <div>
                                <button className="achievements-button" onClick={() => setActiveView("exams")}>
                                    Egzaminy
                                </button>
                            </div>
                            <div>
                                <button className="achievements-button" onClick={() => setActiveView("achievements")}>
                                    Osiągnięcia
                                </button>
                            </div>
                            <div>
                                {isAdmin && (
                                    <Link className="header-link" to="/admin">ADMIN PANEL</Link>
                                )}
                                {isTeacher && (
                                    <button className="achievements-button" onClick={() => navigate("/teacher")}>PANEL NAUCZYCIELA</button>
                                )}
                            </div>
                            <div className="courses-dropdown"
                                onMouseEnter={async () => {
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
                                                onClick={() => { onCourseChosen(c) }}
                                            >
                                                {c.name}
                                            </button>
                                        ))}
                                        {templates.map((t) => (
                                            <button className="template"
                                                key={t.id}
                                                onClick={() => { onTemplateChosen(t) }}
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
                                Witaj, {user?.username} | Punkty: {points}
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