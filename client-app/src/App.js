import { Routes, Route } from "react-router";
import './App.css';
import Home from "./Home";
import Login from "./Login";
import Register from './Register';
import Admin from './Admin';
import { useState, useEffect } from "react";

function App() {

  const [user, setUser] = useState(() => {
      const saved = localStorage.getItem("user") || sessionStorage.getItem("user");
      return saved ? JSON.parse(saved) : null;
  });
  
  useEffect(() => {
    const storedUser = localStorage.getItem("user") || sessionStorage.getItem("user");

    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);
  
  console.log(user);

  return (
    <Routes>
      <Route path="/" element={<Home user={user} setUser={setUser} />} />
      <Route path="/login" element={<Login setUser={setUser} />} />
      <Route path="/register" element={<Register />} />
      <Route path="/admin" element={<Admin user={user}/>} />
    </Routes>
  );
}

export default App;
