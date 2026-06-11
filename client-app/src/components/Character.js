import "./Character.css";
import { addStatPoint } from "../api/characterApi";
import { fetchUserProgress } from "../api/usersApi";

export default function Character({ token, character, setCharacter, points, setPoints }) {
    const handleAddStatPoint = async (stat) => {
        if(points > 0){
            try {
                const updatedCharacter = await addStatPoint(token, stat);
                const updatedPoints = await fetchUserProgress(token);

                setCharacter(updatedCharacter);
                setPoints(updatedPoints.points);
            } catch (error) {
                console.error(error);
            }
        } 
    }

    return (
        <div className="character-stats">
            <h3>Character Stats</h3>

            <div className="stat-row">
                <span> Attack: </span>
                <span>{character.attackPoints}</span>
                <button onClick={() => handleAddStatPoint("ATTACK")}>+</button>
            </div>

            <div className="stat-row">
                <span> Defence: </span>
                <span>{character.defencePoints}</span>
                <button onClick={() => handleAddStatPoint("DEFENCE")}>+</button>
            </div>

            <div className="stat-row">
                <span> Agility: </span>
                <span>{character.agilityPoints}</span>
                <button onClick={() => handleAddStatPoint("AGILITY")}>+</button>
            </div>

            <div className="stat-row">
                <span> Health: </span>
                <span>{character.health}</span>
                <button onClick={() => handleAddStatPoint("HEALTH")}>+</button>
            </div>
        </div>
    );
}