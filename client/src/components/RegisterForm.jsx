import { useState } from "react";
import { useNavigate } from "react-router-dom";

const RegisterForm = ({ setLoggedInUser }) => {
  const navigate = useNavigate();
  const [user, setUser] = useState({
    name: "",
    email: "",
    password: "",
    role: "Patient",
  });
  const [errors, setErrors] = useState([]);

  const handleSubmit = (event) => {
    event.preventDefault();

    fetch("http://localhost:8080/api/users/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user),
    }).then((response) => {
      if (response.status >= 200 && response.status < 300) {
        navigate("/login");
      } else {
        response.json().then((fetchedErrors) => setErrors(fetchedErrors));
      }
    });
  };

  const handleChange = (event) => {
    setUser({ ...user, [event.target.name]: event.target.value });
  };

  return (
    <div className="row">
      {errors.length > 0 && (
        <ul id="errors">
          {errors.map((error) => (
            <li key={error}>{error}</li>
          ))}
        </ul>
      )}

      <h2>Register</h2>

      <div className="col-3"></div>
      <form onSubmit={handleSubmit} className="col-6">
        <div className="form-group">
          <label htmlFor="name-input">Name:</label>
          <input
            name="name"
            className="form-control"
            id="name-input"
            type="text"
            value={user.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="email-input">Email:</label>
          <input
            name="email"
            className="form-control"
            id="email-input"
            type="email"
            value={user.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="password-input">Password:</label>
          <input
            name="password"
            className="form-control"
            id="password-input"
            type="password"
            value={user.password}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="role-input">Role:</label>
          <select
            name="role"
            className="form-control"
            id="role-input"
            value={user.role}
            onChange={handleChange}
          >
            <option value="Patient">Patient</option>
            <option value="Pharma">Pharma</option>
          </select>
        </div>

        <div className="form-group">
          <button className="btn btn-primary mt-2" type="submit">
            Register
          </button>
        </div>
      </form>
    </div>
  );
};

export default RegisterForm;
