import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Pages.css";

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
        if (!response.ok) {
          return response.text().then((text) => {
            throw new Error(text);
          });
        }
        return response.text();
      })
      .then((data) => {
        alert(data); // If login is successful
        localStorage.setItem("isLoggedIn", "true"); // Store login state
        navigate("/feed"); // Redirect to feed page
      })
      .catch((error) => {
        alert(error.message); // Show the error message
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
