const API_URL = "http://localhost:8080";

export const fetchAchievements = async (token) => {
    const res = await fetch(`${API_URL}/achievements`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if(!res.ok) {
        throw new Error("Failed to fetch user achievements");
    }

    return await res.json();
}

export const fetchAllAchievements = async (token) => {
    const res = await fetch(`${API_URL}/achievements/all`,{
        headers: {
            Authorization: `Bearer ${token}`,
        }
    });

    if(!res.ok) {
        throw new Error("Failed to fetch all achievements");
    }

    return await res.json();
}