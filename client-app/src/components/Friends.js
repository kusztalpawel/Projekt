import { useState } from "react";
import "./Friends.css";
import { fetchAddFriend, fetchFriends } from "../api/usersApi";

export default function Friends({ user, setUser, setActiveView, setFriendToFight }) {
    const [friendName, setFriendName] = useState("");
    const [friends, setFriends] = useState(user?.friends);

    const handleAddFriend = async () => {
        
        try {
            const updatedFriends = await fetchAddFriend(user?.token, friendName);
            setFriendName("");
            setFriends(updatedFriends);
            setUser(prev => ({
                    ...prev,
                    friends}));
        } catch(error) {
            console.error(error);
        }
    }

    const handleFightFriend = async (friend) => {
        setFriendToFight(friend);
        setActiveView("arena");
    }

    return (
        <div className="friends-drawer">
            <div className="friends-title">
                Znajomi
            </div>

            <div className="friends-list">
                {friends.map(friend => (
                    <div key={friend.id ?? friend.username} className="friend-item">
                        <div>
                            <div className="friend-name">
                                {friend.username}
                            </div>
                            <div className="friend-stats"> 
                                AT: {friend.character.attackPoints} DF: {friend.character.defencePoints} AG: {friend.character.agilityPoints} 
                            </div>
                        </div>
                        <button onClick={() => handleFightFriend(friend)}>
                            Walcz
                        </button>
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