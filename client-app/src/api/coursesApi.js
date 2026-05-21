const API_URL = "http://localhost:8080";

export const fetchCourses = async (token) => {
    const res = await fetch(`${API_URL}/courses`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
    });

    if (!res.ok) {
        throw new Error("Failed to fetch courses");
    }

    return await res.json();
};

export const createCourse = async (token, courseName) => {
    const res = await fetch(`${API_URL}/courses`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
            name: courseName,
        }),
    });

    if (!res.ok) {
        throw new Error("Failed to create course");
    }

    return await res.json();
} 