import { useState } from "react";
import "./Friends.css";
import { fetchAddFriend } from "../api/usersApi";
import { fightFriend } from "../api/characterApi";

export default function Friends({ token, friends, setFriends }) {
    const [friendName, setFriendName] = useState("");

    const handleAddFriend = async () => {
        setFriendName("");
        try {
            const updatedFriends = await fetchAddFriend(token, friendName);

            setFriends(updatedFriends);
            
        } catch(error) {
            console.error(error);
        }
    }

    const handleFight = async (friendUsername) => {
        try {
            const fightLog = await fightFriend(token, friendUsername);

            console.log(fightLog);
            alert("THE Winner is " + fightLog[fightLog.length - 1].username);

        } catch(error) {
            console.error(error);
        }
    }

    return (
        <div className="friends-drawer">
            <div className="friends-title">
                Znajomi
            </div>

            <div className="friends-list">
                {friends.map(friend => (
                    <div
                        key={friend.id ?? friend.username}
                        className="friend-item"
                    >
                        <div>
                            <div className="friend-name">
                                {friend.username}
                            </div>
                            <div className="friend-stats"> 
                                AT: {friend.character.attackPoints} DF: {friend.character.defencePoints} AG: {friend.character.agilityPoints} 
                            </div>
                        </div>
                        <button
                            onClick={() => handleFight(friend.username)}>Walcz</button>
                    </div>
                ))}
            </div>
            <div className="friends-footer">
                <input
                    type="text"
                    placeholder="Podaj nazwę użytkownika"
                    value={friendName}
                    onChange={(e) => setFriendName(e.target.value)
                    }
                    className="friend-input"
                />

                <button
                    className="add-friend-btn"
                    onClick={handleAddFriend}
                >
                    <span className="add-friend-icon">+</span>

                    <span className="add-friend-text">
                        Dodaj znajomego
                    </span>
                </button>
            </div>
        </div>
    );
}