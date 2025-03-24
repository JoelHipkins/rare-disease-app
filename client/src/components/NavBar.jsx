import "./Navbar.css";
import { useLocation, useNavigate } from "react-router-dom";

function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();

  // Check if the user is logged in
  const isLoggedIn = localStorage.getItem("isLoggedIn");

  if (!isLoggedIn) {
    return null; // If not logged in, don't show the Navbar
  }

  const handleLogout = () => {
    // Clear session data and navigate to the login page
    localStorage.removeItem("isLoggedIn");
    navigate("/"); // Redirect to login page
  };

  return (
    <nav>
      <ul>
        <li>
          <button onClick={() => navigate("/feed")}>Feed</button>
        </li>
        <li>
          <button onClick={() => navigate("/community")}>Community</button>
        </li>
        <li>
          <button onClick={() => navigate("/medicos")}>Medicos</button>
        </li>
        <li>
          <button onClick={() => navigate("/profile")}>Profile</button>
        </li>
        <li>
          <button onClick={handleLogout}>Logout</button>
        </li>
      </ul>
    </nav>
  );
}

export default Navbar;
