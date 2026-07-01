const API_URL = "http://localhost:8080";

export const fetchAchievementMetrics = async (token) => {
    const res = await fetch(`${API_URL}/meta/achievement-metrics`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if(!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
}

export const createAchievement = async (token, achievement) => {
    const res = await fetch(`${API_URL}/achievements`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
            name: achievement.name,
            description: achievement.description,
            code: achievement.code,
            metric: achievement.metric,
            requirement: achievement.requirement
        }),
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
} 

export const getMyExams = async (token) => {
    const res = await fetch(`${API_URL}/exams/created`, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
};

export const getExamResults = async (token, examId) => {
    const res = await fetch(`${API_URL}/exams/${examId}/results`, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
};