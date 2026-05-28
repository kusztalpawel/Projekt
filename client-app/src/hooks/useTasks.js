import { useEffect, useState } from "react";
import { fetchTasks, createTask, toggleTaskApi, deleteTaskApi } from "../api/tasksApi";

export default function useTasks(token, selectedCourse, setSelectedCourse) {
    const [tasks, setTasks] = useState([]);

    useEffect(() => {
        if (!selectedCourse || !token) {
            setTasks([]);
            return;
        }

        const load = async () => {
            try {
                const data = await fetchTasks(token, selectedCourse.id);

                setTasks(data);
            } catch (err) {
                console.error(err);
            }
        };

        load();
    }, [selectedCourse, token]);

    const addTask = async (token, task) => {
        if (!selectedCourse) return;

        const newTask = await createTask(
            token,
            selectedCourse.id,
            task
        );

        setTasks(prev => [...prev, newTask]);
    };

    const toggleTask = async (token, id) => {
        const updated = await toggleTaskApi(token, id);

        setTasks(prev =>prev.map(task => task.id === id ? updated.task : task));
        setSelectedCourse(prev => ({...prev, level: updated.progress.level, experience: updated.progress.experience}));
    };

    const deleteTask = async (token, id) => {

        await deleteTaskApi(token, id);

        setTasks(prev =>
            prev.filter(t => t.id !== id)
        );
    };

    return {
        tasks,
        addTask,
        toggleTask,
        deleteTask,
    };
}