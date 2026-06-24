import "./Character.css";
import { addStatPoint } from "../api/characterApi";
import { fetchUserProgress } from "../api/usersApi";
const API_URL = "http://localhost:8080";

export default function Character({ user, character, setCharacter, points, setPoints, setActiveView }) {
    const handleAddStatPoint = async (stat) => {
        if(points > 0){
            try {
                const updatedCharacter = await addStatPoint(user?.token, stat);
                const updatedPoints = await fetchUserProgress(user?.token);

                setCharacter(updatedCharacter);
                setPoints(updatedPoints.points);
            } catch (error) {
                console.error(error);
            }
        } 
    }

    return (
        <div className="character-stats">
            <h3>Statystyki postaci</h3>
            <div>
                <img className="player-img" src={`${API_URL}/images/skins/${user?.skin}`} alt="" onClick={() => setActiveView("customization")}/>
            </div>
            
            <div className="stat-row">
                <span> Atak: </span>
                <span>{character.attackPoints}</span>
                <button onClick={() => handleAddStatPoint("ATTACK")}>+</button>
            </div>

            <div className="stat-row">
                <span> Obrona: </span>
                <span>{character.defencePoints}</span>
                <button onClick={() => handleAddStatPoint("DEFENCE")}>+</button>
            </div>

            <div className="stat-row">
                <span> Szybkość: </span>
                <span>{character.agilityPoints}</span>
                <button onClick={() => handleAddStatPoint("AGILITY")}>+</button>
            </div>

            <div className="stat-row">
                <span> Życie: </span>
                <span>{character.health}</span>
                <button onClick={() => handleAddStatPoint("HEALTH")}>+</button>
            </div>
        </div>
    );
}