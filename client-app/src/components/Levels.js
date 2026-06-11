import "./Levels.css";

export default function Levels({ currentLevel, currentExperience, experienceNeeded }) {
    return (
        <div className="levels-wrapper">
            <div className="levels-path">
                {Array.from({ length: 6 }, (_, i) => {
                    const level = currentLevel + i;

                    const progress = level === currentLevel ? Math.min((currentExperience / experienceNeeded) * 100, 100) : 0;

                    return (
                        
                            <div
                                key={level}
                                className={`level-node ${ level % 2 === 1 ? "odd" : "even" } ${ i === 0 ? "current-level" : "" }`}
                            >
                                <div className="level-number">
                                    LVL {level}
                                </div>

                                <div className="xp-bar">
                                    <div
                                        className="xp-fill"
                                        style={{
                                            width: `${progress}%`,
                                        }}
                                    />
                                </div>

                                <div className="xp-text">
                                    {currentLevel === 0 ? '' : level === currentLevel ? `${currentExperience}/${experienceNeeded}` : `0/${level * 50}`}
                                </div>
                            </div>
                        
                    );
                })}
            </div>
        </div>
    );
}