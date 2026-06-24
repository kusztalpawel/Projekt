import { fightFriend } from "../api/characterApi";
import { useState, useRef, useEffect } from "react";
import { fetchFriends } from "../api/usersApi";
import "./Arena.css";
const API_URL = "http://localhost:8080";

const Arena = ({ user, setUser, character, friend, setActiveView }) => {
    const playerMaxHP = character.health;
    const enemyMaxHP = friend.character.health;

    const [playerHP, setPlayerHP] = useState(character.health);
    const [enemyHP, setEnemyHP] = useState(friend.character.health);
    const [playerAttacking, setPlayerAttacking] = useState(false); 
    const [enemyAttacking, setEnemyAttacking] = useState(false); 
    const [damageText, setDamageText] = useState(null);
    const [skin, setSkin] = useState(user?.skin);
    const [enemySkin, setEnemySkin] = useState(friend.skinUrl);
    const fightCancelled = useRef(false);
    
    useEffect(() => {
        const loadFriends = async () => {
            const friends = await fetchFriends(user?.token);

            setUser(prev => ({
                ...prev,
                friends
            }));

            setEnemySkin(friends.find(user => user.username === friend.username).skinUrl);
        };

        loadFriends();
    }, []);

    useEffect(() => {
        fightCancelled.current = false;

        return () => {
            fightCancelled.current = true;
        };
    }, []);

    const handleFight = async () => {
        try {
            const fightLog = await fightFriend(user?.token, friend.username);
            setPlayerHP(character.health);
            setEnemyHP(friend.character.health);
            playFight(fightLog);
        } catch(error) {
            console.error(error);
        }
    };

    const sleep = (ms) =>
        new Promise(resolve => setTimeout(resolve, ms));

    const playFight = async (fightLog) => {
        for (const turn of fightLog) {
            if (fightCancelled.current) {
                return;
            }

            if (turn.damage === -1) {
                alert(`${turn.username} wins!`);
                break;
            }

            if (turn.username === user.username) {
                setPlayerAttacking(true);
                await sleep(400);
                setEnemyHP(prev => Math.max(0, prev - turn.damage));

                setDamageText({
                    id: Date.now(),
                    target: "enemy",
                    value: turn.damage
                });

                setPlayerAttacking(false);
            } else {
                setEnemyAttacking(true);

                await sleep(400);

                setPlayerHP(prev => Math.max(0, prev - turn.damage));
                setDamageText({
                    id: Date.now(),
                    target: "player",
                    value: turn.damage
                });

                setEnemyAttacking(false);
            }

            await sleep(600);
        }
    };

    return (
        <div className="arena-page">
            <div className="arena-header">
                <h2>Arena</h2>
                <button className="arena-back" onClick={() => setActiveView("home")}>
                    Powrót
                </button>
            </div>
            <div>
                <button className="arena-back" onClick={() => setActiveView("leaderboard")}>
                    Ranking
                </button>
            </div>

            <div className="arena-content">
                <div className="arena-character">
                    <div className="arena-stats">
                        <h2>
                            {user.username}
                        </h2>
                        <div className="arena-attack-points">
                            AT: {character.attackPoints}
                        </div>
                        <div className="arena-defence-points">
                            DF: {character.defencePoints}
                        </div>
                        <div className="arena-agility-points">
                            AG: {character.agilityPoints}
                        </div>
                    </div>
                    <div className="arena-player-image">
                        <div className="arena-health-container">
                            <div className="arena-health-fill" style={{ width: `${Math.max(0,(playerHP/playerMaxHP)) * 100}%` }}/>
                            <span className="arena-health-text">
                                {playerHP.toFixed(2)} HP
                            </span>
                        </div>
                        <img className={`player-image ${playerAttacking ? "player-attack" : ""}`} src={`${API_URL}/images/skins/${skin}`} alt=""/>
                        {damageText?.target === "player" && (
                            <div  key={damageText.id} className="damage">
                                -{damageText.value}
                            </div>
                        )}
                    </div>
                </div>
                
                <div className="arena-character enemy-side">
                    <div className="arena-stats">
                        <h2>
                            {friend.username}
                        </h2>
                        <div className="arena-attack-points">
                            AT: {friend.character.attackPoints}
                        </div>
                        <div className="arena-defence-points">
                            DF: {friend.character.defencePoints}
                        </div>
                        <div className="arena-agility-points">
                            AG: {friend.character.agilityPoints}
                        </div>
                    </div>
                    <div className="arena-player-image enemy-image">
                        <div className="arena-health-container">
                            <div className="arena-health-fill" style={{ width: `${(enemyHP/enemyMaxHP) * 100}%` }}/>
                            <span className="arena-health-text">
                                {enemyHP.toFixed(2)} HP
                            </span>
                        </div>
                        <img className={`enemy-img ${enemyAttacking ? "enemy-attack" : ""}`} src={`${API_URL}/images/skins/${enemySkin}`} alt=""/>
                        {damageText?.target === "enemy" && (
                            <div  key={damageText.id} className="damage enemy-damage">
                                -{damageText.value}
                            </div>
                        )}
                    </div>
                </div>
            </div>
            <button className="arena-fight-button" onClick={() => handleFight()}>
                Zacznij walkę
            </button>
        </div>
    );
};

export default Arena;