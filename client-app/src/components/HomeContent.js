import Levels from "./Levels.js";
import Character from "./Character.js"
import Tasks from "./Tasks.js";
import "./HomeContent.css";

export default function HomeContent({user, selectedCourse, setSelectedCourse, points, setPoints, character, setCharacter, setActiveView}) {
    return (
        <div className="home-content">
            <Tasks user={user} setPoints={setPoints} selectedCourse={selectedCourse} setSelectedCourse={setSelectedCourse}/>
            <Levels currentLevel={selectedCourse?.level ?? 0} currentExperience={selectedCourse?.experience ?? 0} experienceNeeded={selectedCourse?.experienceNeeded ?? 1000000000} />
            <Character user={user} character={character} setCharacter={setCharacter} points={points} setPoints={setPoints} setActiveView={setActiveView}/>
        </div>
    )
}