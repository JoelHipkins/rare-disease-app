import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Navbar from "./components/Navbar/NavBar";
import Registration from "./pages/LoginRegistrationPage/Registration";
import Login from "./pages/LoginRegistrationPage/LoginPage";
import Feed from "./pages/FeedPage/FeedPage";
import Community from "./pages/Community-Page/CommunityPage";
import Medicos from "./pages/MedicosPage/MedicosPage";
import Profile from "./pages/ProfilePage/Profile";
import ProtectedRoute from "./components/ProtectedRoute";
import "./App.css";
import { motion } from "framer-motion";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<Registration />} />

        <Route
          path="/feed"
          element={
            <motion.div
              key="feed"
              initial={{ opacity: 0 }} // Initial state: start as invisible
              animate={{ opacity: 1 }} // Animate to fully visible
              exit={{ opacity: 0 }} // Exit animation: fade out
              transition={{ duration: 0.8 }} // Duration of the animation
            >
              <>
                <ProtectedRoute>
                  <Navbar />
                  <Feed />
                </ProtectedRoute>
              </>
            </motion.div>
          }
        />

        <Route
          path="/community"
          element={
            <motion.div
              key="community"
              initial={{ opacity: 0 }} // Initial state: start as invisible
              animate={{ opacity: 1 }} // Animate to fully visible
              exit={{ opacity: 0 }} // Exit animation: fade out
              transition={{ duration: 0.8 }} // Duration of the animation
            >
              <>
                <ProtectedRoute>
                  <Navbar />
                  <Community />
                </ProtectedRoute>
              </>
            </motion.div>
          }
        />

        <Route
          path="/medicos"
          element={
            <motion.div
              key="medicos"
              initial={{ opacity: 0 }} // Initial state: start as invisible
              animate={{ opacity: 1 }} // Animate to fully visible
              exit={{ opacity: 0 }} // Exit animation: fade out
              transition={{ duration: 0.8 }} // Duration of the animation
            >
              <>
                <ProtectedRoute>
                  <Navbar />
                  <Medicos />
                </ProtectedRoute>
              </>
            </motion.div>
          }
        />

        <Route
          path="/profile"
          element={
            <motion.div
              key="profile"
              initial={{ opacity: 0 }} // Initial state: start as invisible
              animate={{ opacity: 1 }} // Animate to fully visible
              exit={{ opacity: 0 }} // Exit animation: fade out
              transition={{ duration: 0.8 }} // Duration of the animation
            >
              <>
                {" "}
                <ProtectedRoute>
                  <Navbar />
                  <Profile />
                </ProtectedRoute>
              </>
            </motion.div>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
