const API_URL = "http://localhost:8080";

export const addStatPoint = async(token, stat) => {
    const res = await fetch(`${API_URL}/character/upgrade/${stat}`, {
        method: "POST",
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
}

export const fightFriend = async (token, friendUsername) => {
    const res = await fetch(`${API_URL}/character/fight/${encodeURIComponent(friendUsername)}`,
    {
        method: "POST",
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    if(!res.ok){
        throw new Error(await res.text());
    }

    return await res.json();
}