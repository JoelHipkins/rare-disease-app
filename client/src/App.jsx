import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Navbar from "./components/Navbar/NavBar";
import Registration from "./pages/Registration";
import Login from "./pages/LoginPage";
import Feed from "./pages/FeedPage";
import Community from "./pages/CommunityPage";
import Medicos from "./pages/MedicosPage";
import Profile from "./pages/Profile";
import ProtectedRoute from "./components/ProtectedRoute";
import "./App.css";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<Registration />} />

        <Route
          path="/feed"
          element={
            <>
              <ProtectedRoute>
                <Navbar />
                <Feed />
              </ProtectedRoute>
            </>
          }
        />

        <Route
          path="/community"
          element={
            <>
              <ProtectedRoute>
                <Navbar />
                <Community />
              </ProtectedRoute>
            </>
          }
        />

        <Route
          path="/medicos"
          element={
            <>
              <ProtectedRoute>
                <Navbar />
                <Medicos />
              </ProtectedRoute>
            </>
          }
        />

        <Route
          path="/profile"
          element={
            <>
              {" "}
              <ProtectedRoute>
                <Navbar />
                <Profile />
              </ProtectedRoute>
            </>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
