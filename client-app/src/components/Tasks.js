import { useState } from "react";
import useTasks from "../hooks/useTasks.js";
import { HiTrash } from "react-icons/hi2";
import { toast } from "react-toastify";
import "./Tasks.css";

export default function Tasks({ user, setPoints, selectedCourse, setSelectedCourse }) {
    const { tasks, addTask, toggleTask, deleteTask } = useTasks(user?.token, selectedCourse, setSelectedCourse, setPoints);
    const DIFFICULTY_EXP = {
        EASY: 50,
        MEDIUM: 100,
        HARD: 200
    };
    const alphaNumericRegex = /^[A-Za-z0-9]+$/;

    const [newTask, setNewTask] = useState({name: "", exp: DIFFICULTY_EXP.EASY});

    const handleAddTask = async () => {
        if (!selectedCourse) {
            toast.error("Najpierw wybierz kurs!");
            return;
        }

        if (!newTask.name.trim()) {
            toast.error("Podaj nazwę zadania");
            return;
        }

        if (!alphaNumericRegex.test(newTask.name)) {
            toast.error("Zadanie powinno zawierać tylko litery lub cyfry.");
            return;
        }

        await addTask(user?.token, newTask.name, newTask.exp);
        setNewTask({name: "", exp: DIFFICULTY_EXP.EASY});
    };

    const handleToggleTask = (id) => {
        toggleTask(user, id);
    };

    const handleDeleteTask = (id) => {
        deleteTask(user?.token, id);
    };

    return (
        <div className="tasks-container">
            <h1>Twoje zadania</h1>

            <div className="task-input">
                <input
                    type="text"
                    placeholder="Dodaj nowe zadanie..."
                    value={newTask.name}
                    onChange={(e) => setNewTask({name: e.target.value, exp: newTask.exp})}
                />
                <select
                    name="difficulty"
                    value={newTask.exp}
                    onChange={(e) => setNewTask({name: newTask.name, exp: e.target.value})}
                >
                    <option value={DIFFICULTY_EXP.EASY}>Easy</option>
                    <option value={DIFFICULTY_EXP.MEDIUM}>Medium</option>
                    <option value={DIFFICULTY_EXP.HARD}>Hard</option>
                </select>

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
                            
                        </div>
                        <div className="task-right">
                            <span className="task-points">
                                {task.points} EXP
                            </span>
                            <button className="delete-btn" onClick={() => handleDeleteTask(task.id)}>
                                <HiTrash />
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    )
}