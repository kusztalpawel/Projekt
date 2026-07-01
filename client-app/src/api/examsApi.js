const API_URL = "http://localhost:8080";

export const fetchExams = async (token) => {
    
};

export const createExam = async (token, exam) => {
    const res = await fetch(`${API_URL}/exams`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(exam)
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
};

export const createQuestion = async (token, question) => {
    const res = await fetch(`${API_URL}/exams/question`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(question)
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
};

export const deleteQuestion = async (token, questionId) => {
    const res = await fetch(`${API_URL}/exams/question/${questionId}`, {
        method: "DELETE",
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }
};

export const deleteExam = async (token, examId) => {
    const res = await fetch(`${API_URL}/exams/${examId}`,
        {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    );

    if (!res.ok) {
        throw new Error(await res.text());
    }
};

export const deleteAttempt = async (token, examId, attemptId) => {
    const res = await fetch(`${API_URL}/exams/${examId}/attempts/${attemptId}`,
        {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    );

    if (!res.ok) {
        throw new Error(await res.text());
    }
};

export const fetchQuestions = async (token, examId) => {

    const res = await fetch(`${API_URL}/exams/questions/${examId}`, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
};

export const fetchTestQuestions = async (token, attemptId) => {

    const res = await fetch(`${API_URL}/exams/questions/test/${attemptId}`, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

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

export const joinExam = async (token, code) => {
    const res = await fetch(`${API_URL}/exams/join`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({ code })
    });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return await res.json();
};

export const submitExam = async (token, attemptId, payload) => {
    const res = await fetch(`${API_URL}/exams/${attemptId}/submit`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(payload)
    });

    if (!res.ok) {
       throw new Error(await res.text());
    }

    return await res.json();
};

export const fetchPowerUps = async (token) => {
    const res = await fetch(`${API_URL}/exams/powers`,{
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return res.json();
};

export const buyPowerUp = async (token, powerId) => {
    const res = await fetch(`${API_URL}/exams/buy/${powerId}`,{
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return res.json();
};

export const applyPowerUp = async (token, attemptId, powerId) => {
    const res = await fetch(`${API_URL}/exams/attempt/${attemptId}/use/${powerId}`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    );

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return res.json();
};

export const fetchPowersUsedInAttempt = async (token, attemptId) => {
    const res = await fetch(`${API_URL}/exams/attempt/${attemptId}`,{
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

    if (!res.ok) {
        throw new Error(await res.text());
    }

    return res.json();
};