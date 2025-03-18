import { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import RegisterForm from "./components/RegisterForm.jsx";
import LoginForm from "./components/LoginForm.jsx";
import NavBar from "./components/NavBar.jsx";
import "./App.css";

function App() {
  const [loggedInUser, setLoggedInUser] = useState(null);
  const [hasFinishedCheckingLocalStorage, setHasFinishedCheckingLocalStorage] =
    useState(false);

  useEffect(() => {
    if (localStorage.getItem("loggedInUser")) {
      setLoggedInUser(JSON.parse(localStorage.getItem("loggedInUser")));
    }
    setHasFinishedCheckingLocalStorage(true);
  }, []);

  if (!hasFinishedCheckingLocalStorage) {
    return null; // Prevents flickering before localStorage check is complete
  }

  return (
    <Router>
      <div className="container">
        <NavBar loggedInUser={loggedInUser} setLoggedInUser={setLoggedInUser} />
        <main>
          <Routes>
            <Route path="/" element={<div>Welcome to the system</div>} />
            <Route
              path="/signup"
              element={
                loggedInUser === null ? (
                  <RegisterForm setLoggedInUser={setLoggedInUser} />
                ) : (
                  <Navigate to="/" />
                )
              }
            />
            <Route
              path="/login"
              element={
                loggedInUser === null ? (
                  <LoginForm setLoggedInUser={setLoggedInUser} />
                ) : (
                  <Navigate to="/" />
                )
              }
            />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
