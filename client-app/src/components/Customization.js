import { useState, useEffect } from "react";
import { getSkins, selectSkin } from "../api/usersApi";
import "./Customization.css";

const Customization = ({ user, setUser, setActiveView }) => {
    const [skins, setSkins] = useState();

    useEffect(() => {
        const loadSkins = async () => {
            const res = await getSkins(user?.token);
    
            setSkins(res);
        };
    
        loadSkins();
    }, [user?.token]);

    const handleSelectSkin = async (chosenSkin) => { 
        if(chosenSkin.isUnlocked){
            try {
                const newSkin = await selectSkin(user?.token, chosenSkin.skinUrl);
                const skin = newSkin.skinUrl;

                setUser(prev => ({
                    ...prev,
                    skin
                }));
            } catch (err) {
                console.error(err);
            }
        }
    }

    console.log(user.skin);

    return (
        <div className="customization-page">

            <div className="customization-header">
                <h2>Customization</h2>

                <button
                    className="customization-home"
                    onClick={() => setActiveView("home")}
                >
                    Powrót
                </button>
            </div>

            <div className="skins-grid">

                {skins?.map((skin) => (
                    <div key={skin.skinUrl} className={`skin-card  ${!skin.isUnlocked ? "locked" : ""} ${user?.skin === skin.skinUrl ? "chosen" : ""}`} onClick={()=> handleSelectSkin(skin)}>
                        <img src={`http://localhost:8080/images/skins/${skin.skinUrl}`} alt={skin.skinUrl}/>

                        {!skin.isUnlocked && (
                            <div className="skin-requirement">
                                ZABLOKOWANY
                            </div>
                        )}
                    </div>
                ))}
            </div>

        </div>
    );
};

export default Customization;