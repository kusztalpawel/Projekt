import { useState } from "react";
import { fetchAddFriend } from "../api/usersApi";
import { toast } from "react-toastify";
import "./Friends.css";
const API_URL = "http://localhost:8080";

export default function Friends({ user, setUser, setActiveView, setFriendToFight }) {
    const [friendName, setFriendName] = useState("");
    const [friends, setFriends] = useState(user?.friends);
    const [visible, setVisible] = useState(false);
    const alphaNumericRegex = /^[A-Za-z0-9]+$/;

    const handleAddFriend = async () => {
        if(!friendName.trim()){
            toast.error("Nazwa znajomego nie może być pusta!");
            return;
        }

        if (!alphaNumericRegex.test(friendName)) {
            toast.error("Nazwa znajomego powinna zawierać tylko litery lub cyfry.");
            return;
        }

        try {
            const updatedFriends = await fetchAddFriend(user?.token, friendName);
            setFriendName("");
            setFriends(updatedFriends);
            setUser(prev => ({
                    ...prev,
                    friends}));
            toast.success("Dodano znajmego!");
        } catch(error) {
            toast.error(error.message);
        }
    }

    const handleFightFriend = async (friend) => {
        setFriendToFight(friend);
        setActiveView("arena");
    }

    return (
        <div className="friends-drawer" onMouseEnter={() => setVisible(true)} onMouseLeave={() => setVisible(false)}>
            <div className="friends-title">
                Znajomi
            </div>

            <div className="friends-list">
                {friends.map(friend => (
                    <div key={friend.id ?? friend.username} className="friend-item">
                        <div className="friend-data">
                            <img className="friend-icon" src={`${API_URL}/images/skins/${friend.skinUrl}`} alt=""/>
                            {visible && <div>
                                <div className="friend-name">
                                    {friend.username}
                                </div>
                                <div className="friend-stats"> 
                                    AT: {friend.character.attackPoints} DF: {friend.character.defencePoints} AG: {friend.character.agilityPoints} 
                                </div>
                            </div>} 
                        </div>
                        {visible && <button onClick={() => handleFightFriend(friend)}>
                            Walcz
                        </button>}
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