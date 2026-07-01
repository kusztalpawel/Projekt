import "./Character.css";
import { addStatPoint } from "../api/characterApi";
import { fetchUserProgress } from "../api/usersApi";
import { TbSwords, TbShieldFilled, TbHeartFilled } from "react-icons/tb";
import { GiWingfoot } from "react-icons/gi";
import { FaPlusCircle } from "react-icons/fa";
import { toast } from "react-toastify";

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
                toast.error(error.message);
            }
        } else {
            toast.error("Niewystarczająca liczba punktów!");
        }
    }

    return (
        <div className="character-stats">
            <h3>Statystyki postaci</h3>
            <div>
                <img className="player-img" src={`${API_URL}/images/skins/${user?.skin}`} alt="" onClick={() => setActiveView("customization")}/>
            </div>
            
            <div className="stat-row">
                <span> <TbSwords /> ATAK</span>
                <span>{character.attackPoints}</span>
                <FaPlusCircle className="plusIcon" onClick={() => handleAddStatPoint("ATTACK")}/>
            </div>

            <div className="stat-row">
                <span> <TbShieldFilled /> OBRONA</span>
                <span>{character.defencePoints}</span>
                <FaPlusCircle className="plusIcon" onClick={() => handleAddStatPoint("DEFENCE")}/>
            </div>

            <div className="stat-row">
                <span> <GiWingfoot /> SZYBKOŚĆ </span>
                <span>{character.agilityPoints}</span>
                <FaPlusCircle className="plusIcon" onClick={() => handleAddStatPoint("AGILITY")}/>
            </div>

            <div className="stat-row">
                <span> <TbHeartFilled /> PUNKTY ŻYCIA</span>
                <span>{character.health}</span>
                <FaPlusCircle className="plusIcon" onClick={() => handleAddStatPoint("HEALTH")}/>
            </div>
        </div>
    );
}