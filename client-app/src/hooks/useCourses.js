import { useEffect, useState } from "react";
import { createCourse, fetchCourses } from "../api/coursesApi";

export default function useCourses(token) {
    const [courses, setCourses] = useState([]);

    useEffect(() => {
        if (!token) return;

        const load = async () => {
            try {
                const data = await fetchCourses(token);
                setCourses(data);
            } catch (err) {
                console.error("Error fetching courses:", err);
            }
        };

        load();
    }, [token]);

    const addCourse = async (name) => {

        const newCourse =
            await createCourse(token, name);

        setCourses(prev => [...prev, newCourse]);
    };

    return {courses, addCourse};
}