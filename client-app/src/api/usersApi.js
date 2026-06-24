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

export const fetchFriends = async (token) => {
    const res = await fetch(`${API_URL}/users/friend`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

    if (!res.ok) {
        throw new Error("Failed to fetch friends");
    }

    return await res.json();
};

export const fetchLeaderboard = async (token) => {
    const res = await fetch(`${API_URL}/users/leaderboard`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    if (!res.ok) {
        throw new Error("Failed to fetch leaderboard");
    }

    return await res.json();
};

export const selectSkin = async (token, skinUrl) => {
    const res = await fetch(
        `${API_URL}/users/skin/${encodeURIComponent(skinUrl)}`,
        {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    );

    if (!res.ok) {
        throw new Error("Failed to select skin");
    }

    return res.json();
};

export const getSkins = async (token) => {
    const res = await fetch(
        `${API_URL}/users/skins`,
        {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    );

    if (!res.ok) {
        throw new Error("Failed to fetch skins");
    }

    return await res.json();
};