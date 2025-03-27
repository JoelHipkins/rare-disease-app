import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./LoginRegistration.css";

const Login = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const navigate = useNavigate(); // To navigate programmatically

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    fetch("http://localhost:8080/api/users/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    })
      .then((response) => {
        // If response is not OK (i.e., 400), handle the error as plain text
        if (!response.ok) {
          return response.text().then((text) => {
            // Display the plain text error message from the backend
            throw new Error(text);
          });
        }
        // If response is OK, parse it as JSON for successful response (JWT and message)
        return response.json();
      })
      .then((data) => {
        // On success, alert the message and store the JWT
        alert(data.message); // Show success message (e.g., "Welcome bilalbhai!")
        localStorage.setItem("jwt", data.jwt); // Store the JWT token

        navigate("/feed"); // Redirect to the feed page
      })
      .catch((error) => {
        // Handle error, which contains the message from the backend
        alert(error.message); // This will show "Incorrect credentials!" or any other error message
      });
  };

  const handleSignupRedirect = () => {
    navigate("/signup"); // Redirect to signup page
  };

  return (
    <div className="login-container">
      <h1 className="login-heading">Login</h1>
      <form className="login-form" onSubmit={handleSubmit}>
        <label htmlFor="email">Email:</label>
        <input
          type="email"
          id="email"
          name="email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <label htmlFor="password">Password:</label>
        <input
          type="password"
          id="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <button type="submit">Login</button>
      </form>

      <button className="signup-btn" onClick={handleSignupRedirect}>
        Sign Up
      </button>
    </div>
  );
};

export default Login;
