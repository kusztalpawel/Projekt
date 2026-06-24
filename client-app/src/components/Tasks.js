import { useState } from "react";
import useTasks from "../hooks/useTasks.js";

export default function Tasks({ user, setPoints, selectedCourse, setSelectedCourse }) {
    const [newTask, setNewTask] = useState("");
    const { tasks, addTask, toggleTask, deleteTask } = useTasks(user?.token, selectedCourse, setSelectedCourse, setPoints);

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

    return (
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
    )
}