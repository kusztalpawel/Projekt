import { Routes, Route } from "react-router";
import './App.css';
import Home from "./Home";
import Login from "./Login";
import Register from './Register';
import { useState, useEffect } from "react";

function App() {

  const [user, setUser] = useState(null);

  useEffect(() => {
    const storedUser = localStorage.getItem("user");

    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  return (
    <Routes>
      <Route path="/" element={<Home user={user} setUser={setUser} />} />
      <Route path="/login" element={<Login setUser={setUser} />} />
      <Route path="/register" element={<Register />} />
    </Routes>
  );
}

export default App;
