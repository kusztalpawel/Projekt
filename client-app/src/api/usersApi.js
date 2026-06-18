const API_URL = "http://localhost:8080";

export const fetchUserLogin = async (username, password) => {
    const res = await fetch(`${API_URL}/users/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json", },
        body: JSON.stringify({ username, password }),
    });

    if (!res.ok) {
        throw new Error(res.message || "Login failed");
    }

    return await res.json();
}

export const fetchUserProgress = async (token) => {
    const res = await fetch(`${API_URL}/users/progress`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!res.ok) {
        throw new Error("Failed to fetch user progress");
    }

    return await res.json();
};

export const fetchAddFriend = async (token, friendUsername) => {
    const res = await fetch(`${API_URL}/users/friend/${encodeURIComponent(friendUsername)}`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

    if (!res.ok) {
        throw new Error("Failed to add friend");
    }

    return await res.json();
};

export const fetchAchievements = async (token) => {
    const res = await fetch(`${API_URL}/achievements`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if(!res.ok) {
        throw new Error("Failed to fetch user achievements");
    }
}