import { useEffect, useState } from "react";
import { fetchTasks, createTask, deleteTaskApi } from "../api/tasksApi";

export default function useTasks(token, selectedCourse) {
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

    const toggleTask = async (id) => {

        /*const updated = await toggleTaskApi(
            user.token,
            id
        );

        setTasks(prev =>
            prev.map(t =>
                t.id === id ? updated : t
            )
        );*/
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