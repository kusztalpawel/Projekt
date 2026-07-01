import { Routes, Route } from "react-router";
import { ToastContainer } from "react-toastify";
import { useState, useEffect } from "react";
import Home from "./Home";
import Login from "./Login";
import Register from './Register';
import Admin from './Admin';
import TeacherPanel from "./TeacherPanel";
import './App.css';

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

  return (
    <>
      <Routes>
        <Route path="/" element={<Home user={user} setUser={setUser} />} />
        <Route path="/login" element={<Login setUser={setUser} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/admin" element={<Admin user={user}/>} />
        <Route path="/teacher" element={<TeacherPanel user={user}/>} />
      </Routes>
      <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} closeOnClick pauseOnHover theme="light"/>
    </>
    
  );
}

export default App;
