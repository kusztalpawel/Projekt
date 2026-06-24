import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import useCourses from "./hooks/useCourses";
import { fetchAchievementMetrics, createAchievement } from "./api/adminApi";
import "./Login.css";
import { createTask } from "./api/tasksApi";

const Admin = ({user}) => {
    const token = user?.token;

    const [achievementForm, setAchievementForm] = useState({name: "",description: "", metric: "",code: "",requirement: ""});

    const [courseForm, setCourseForm] = useState({name: ""});

    const { courses, addCourse } = useCourses(token);
    const [ chosenCourse, setChosenCourse] = useState();

    const [taskForm, setTaskForm] = useState({name: "", exp: ""});
    const [metrics, setMetrics] = useState();

    const createHandleChange = (setState) => (e) => {
        setState(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const handleAchievementChange = createHandleChange(setAchievementForm);
    const handleCourseChange = createHandleChange(setCourseForm);
    const handleCourseSet = createHandleChange(setChosenCourse);
    const handleTaskChange = createHandleChange(setTaskForm);

    const handleAddCourse = async () => {
        try{
            await addCourse(courseForm.name);
            setCourseForm({name: ""});
            alert("Course created");
        } catch (err){
            console.error(err);
        }
    }

    const handleAddAchievement = async () => {
        try{
            await createAchievement(token, achievementForm);
            setAchievementForm({name: "",description: "", metric: achievementForm.metric, code: "", requirement: ""});
            alert("Achievement added");
        } catch (err){
            console.error(err);
        }
    }

    const handleAddTask = async () => {
        try {
            await createTask(token, taskForm.courseId, taskForm.name, taskForm.exp);
            setTaskForm({name: "", exp: "", courseId: taskForm.courseId});
            alert("Task added");
        } catch (err) {
            console.error(err);
        }
    }

    const loadMeta = async () => {
        if (!token) return;
    
        try {
            const data = await fetchAchievementMetrics(token);
            setMetrics(data);
        } catch (err) {
            console.error("Error fetching metrics:", err);
        }
    };

    useEffect(() => {
        loadMeta();
    }, []);

    return (
        <div>
            <div>
                <h2>Admin Panel</h2>
                <Link to="/">Back</Link>
            </div>

            <div>
                <h4>Achievements</h4>
                <input name="name" placeholder="Name"  value={achievementForm.name} onChange={handleAchievementChange} />
                <input name="description" placeholder="Description" value={achievementForm.description} onChange={handleAchievementChange} />
                <input name="code" placeholder="Code" value={achievementForm.code} onChange={handleAchievementChange} />
                <input name="requirement" placeholder="Requirement" value={achievementForm.requirement} onChange={handleAchievementChange} />
                <select name="metric" value={achievementForm.metric} onChange={handleAchievementChange}>
                    {metrics?.map(metric => (
                        <option key={metric} value={metric}>
                            {metric}
                        </option>
                    ))}
                </select>
                <button onClick={handleAddAchievement}>
                    Add Achievement
                </button>
            </div>
            <div>
                <h4>Courses</h4>
                <input name="name" placeholder="Name" value={courseForm.name} onChange={handleCourseChange} />
                <button onClick={handleAddCourse}>
                    Add Course
                </button>
            </div>
            <div>
                <h4>Tasks</h4>
                <select name="courseId" id={taskForm.courseId} onChange={handleTaskChange}>
                    <option value="">Select course</option>

                    {courses.map(course => (
                        <option key={course.id} value={course.id}>
                            {course.name}-{course.id}
                        </option>
                    ))}
                </select>
                <input name="name" placeholder="Name" value={taskForm.name} onChange={handleTaskChange} />
                <input name="exp" placeholder="Experience" value ={taskForm.exp} onChange={handleTaskChange} />
                <button onClick={handleAddTask}>
                    Add Task
                </button>
            </div>
        </div>
    );
};

export default Admin;