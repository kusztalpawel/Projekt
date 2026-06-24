import { useEffect, useState } from "react";
import { fetchLeaderboard } from "../api/usersApi";
import "./Leaderboard.css";

const Leaderboard = ({ user, setActiveView }) => {

    const [leaderboard, setLeaderboard] = useState([]);

    useEffect(() => {
        const loadLeaderboard = async () => {
            try {
                const data = await fetchLeaderboard(user.token);
                setLeaderboard(data);
            } catch (err) {
                console.error(err);
            }
        };

        loadLeaderboard();
    }, [user.token]);

    return (
        <div className="leaderboard-page">

            <div className="leaderboard-header">
                <h2>Ranking</h2>

                <button className="leaderboard-back" onClick={() => setActiveView("arena")}>
                    Powrót
                </button>
            </div>

            <div className="leaderboard-list">
                {leaderboard.map((player, index) => (
                    <div key={player.username} className="leaderboard-item">
                        <div className="leaderboard-position">
                            {index + 1}
                        </div>
                        <div className="leaderboard-name">
                            {player.username}
                        </div>
                        <div className="leaderboard-score">
                            {player.wins}W / {player.loses}L | {(player.wins/(player.wins + player.loses))*100}% wygranych
                        </div>
                    </div>
                ))}
            </div>

        </div>
    );
};

export default Leaderboard;