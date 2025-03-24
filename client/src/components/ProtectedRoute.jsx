import { Navigate } from "react-router-dom";

// ProtectedRoute component to protect routes that need authentication
function ProtectedRoute({ children }) {
  const isJwtToken = localStorage.getItem("jwt");

  if (!isJwtToken) {
    return <Navigate to="/" replace />; // Redirect to Login page if not logged in
  }

  return children; // Render the child component (the page) if logged in
}

export default ProtectedRoute;
