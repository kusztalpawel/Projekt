import { useEffect, useState } from "react";
import { createCourse, fetchCourses, fetchCourseTemplates, fetchAllCourses, enroll } from "../api/coursesApi";
import { jwtDecode } from "jwt-decode";

export default function useCourses(token) {
    const [courses, setCourses] = useState([]);
    const [templates, setTemplates] = useState([]);

    const loadCourses = async () => {
        if (!token) return;

        try {
            const data = await fetchCourses(token);
            setCourses(data);
        } catch (err) {
            console.error("Error fetching courses:", err);
        }
    };

    const loadTemplates = async () => {
        if (!token) return;
        
        try {
            const data = await fetchCourseTemplates(token);
            setTemplates(data);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        loadCourses();
        loadTemplates();
    }, [token]);

    const enrollCourse = async (courseId) => {
        try {
            const newCourse = await enroll(token, courseId);

            setCourses(prev => [...prev, newCourse]);
        } catch (err) {
            console.error("Enroll failed:", err);
        }
    };

    const addCourse = async (name) => {
        const newCourse = await createCourse(token, name);

        setCourses(prev => [...prev, newCourse]);
    };

    useEffect(() => {
        const loadAll = async () => {
            try {
                if(jwtDecode(token).role !== "ADMIN"){
                    return;
                }
                const data = await fetchAllCourses(token);
                setCourses(data);
            } catch (err) {
                console.error(err);
            }
        };

        loadAll();
    }, [token]);

    return {courses, addCourse, templates, enrollCourse, loadCourses, loadTemplates};
}