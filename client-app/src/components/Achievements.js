import React, { useEffect, useState } from "react";
import { fetchAchievements, fetchAllAchievements } from "../api/achievementsApi";
import "./Achievements.css";

const Achievements = ({user, setUser, setActiveView}) => {
    const [allAchievements, setAllAchievements] = useState();

    useEffect(() => {
        const loadAllAchievements = async () => {
            try {
                const res = await fetchAllAchievements(user.token);
                setAllAchievements(res);
            } catch (err) {
                console.error(err);
            }
        }

        loadAllAchievements();
    }, [user?.token]);

    useEffect(() => {
        const loadAchievements = async () => {
            try {
                const achievements = await fetchAchievements(user.token);
                setUser(prev => ({
                    ...prev,
                    achievements
                }));
            } catch (err) {
                console.error(err);
            }
        };

        loadAchievements();
    }, [user?.token, setUser]);

    const formatDate = (date) => {
        return date.replace("T", " ").substring(0, 16);
    };

    return (
        <div className="achievements-page">
            <div className="achievements-header">
                <div className="achievements-title">
                    Osiągnięcia
                </div>
                <button className="back-button" onClick={() => setActiveView("home")}>
                    Wróć
                </button>
            </div>
            <div className="achievements-list">
                {user.achievements.map(achievement => (
                    <div key={achievement.name} className="achievement-item">
                        <div>
                            <div className="achievement-name">
                                {achievement.name}
                            </div>
                            <div className="achievement-description"> 
                                {achievement.description}
                            </div>
                            <div className="achievement-unlocked-at"> 
                                Zdobyto: {formatDate(achievement.unlockedAt)}
                            </div>
                        </div>
                    </div>
                ))}
                {allAchievements?.filter(achievement =>!(user?.achievements ?? []).some(unlocked => unlocked.name === achievement.name)).map(achievement => (
                    <div key={achievement.name} className="achievement-item locked">
                        <div>
                            <div className="achievement-name">
                                {achievement.name}
                            </div>
                            <div className="achievement-description"> 
                                {achievement.description}
                            </div>
                            <div className="achievement-unlocked-at"> 
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Achievements;