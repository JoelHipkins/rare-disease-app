import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate
import "./Pages.css";

const Registration = () => {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    role: "PATIENT",
  });

  const navigate = useNavigate(); // Initialize navigate

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Submitted Data:", formData);
    fetch("http://localhost:8080/api/users/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    })
      .then((response) => response.text())
      .then((data) => {
        alert(data); // If registration is successful, show the success message
        if (data === "User registered successfully!") {
          navigate("/"); // Redirect to the login page after successful registration
        }
      })
      .catch((error) => console.error("Error:", error));
  };

  return (
    <div className="registration-container">
      <h1 className="registration-heading">Register Yourself</h1>
      <form className="registration-form" onSubmit={handleSubmit}>
        <label htmlFor="username">Username:</label>
        <input
          type="text"
          id="username"
          name="username"
          value={formData.username}
          onChange={handleChange}
          required
        />

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

        <label htmlFor="role">Role:</label>
        <select
          id="role"
          name="role"
          value={formData.role}
          onChange={handleChange}
          required
        >
          <option value="PATIENT">Patient</option>
          <option value="DOCTOR">Doctor</option>
          <option value="PHARMA">Pharma</option>
        </select>

        <button type="submit">Register</button>
      </form>
    </div>
  );
};

export default Registration;
