const API_URL = "http://localhost:8080";

export const fetchTasks = async (token, selectedCourse) => {
    const res = await fetch(
        `${API_URL}/tasks/course/${selectedCourse}`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
};

export const createTask = async (token, courseId, taskName, taskExp) => {
    const res = await fetch(
        `${API_URL}/tasks`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
                name: taskName,
                points: taskExp,
                courseId,
            }),
        }
    );

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
};

export const toggleTaskApi = async (token, taskId) => {
    const res = await fetch(
        `${API_URL}/tasks/${taskId}`,
        {
            method: "PATCH",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    if(!res.ok){
        throw new Error(await res.text());
    }

    return await res.json();
}

export const deleteTaskApi = async (token, taskId) => {
    const res = await fetch(
        `${API_URL}/tasks/${taskId}`,
        {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    if(!res.ok){
        throw new Error(await res.text());
    }
};