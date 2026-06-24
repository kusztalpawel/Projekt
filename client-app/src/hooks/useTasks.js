import { useEffect, useState } from "react";
import { fetchTasks, createTask, toggleTaskApi, deleteTaskApi } from "../api/tasksApi";
import { fetchUserProgress } from "../api/usersApi";

export default function useTasks(token, selectedCourse, setSelectedCourse, setPoints) {
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
            task,
            50
        );

        setTasks(prev => [...prev, newTask]);
    };

    const toggleTask = async (user, id) => {
        const previousLevel = selectedCourse.level;

        const updated = await toggleTaskApi(user?.token, id);
        
        setTasks(prev =>prev.map(task => task.id === id ? updated.task : task));
        setSelectedCourse(prev => ({...prev, level: updated.progress.level, experience: updated.progress.experience, experienceNeeded: updated.progress.experienceNeeded}));

        if (updated.progress.level > previousLevel) {
            const userProgress = await fetchUserProgress(user?.token);

            setPoints(userProgress.points);
        }
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