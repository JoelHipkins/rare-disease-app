import React, { useState } from "react";
// Import the jwt-decode library
import "./CommunityPage.css";
import { jwtDecode } from "jwt-decode";

const CreateCommunityModal = ({ onClose }) => {
  const [communityName, setCommunityName] = useState("");
  const [communityDescription, setCommunityDescription] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const token = localStorage.getItem("jwt"); // Get JWT token from local storage

  // Decode the token to get user_id (assumed that user_id is stored in token)
  const decodedToken = jwtDecode(token);
  const userId = decodedToken.userId; // Adjust this according to the actual key in the token

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    const communityData = {
      communityName,
      communityDescription,
      userId, // Pass the userId extracted from the token
    };

    try {
      const response = await fetch("http://localhost:8080/api/communities", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(communityData),
      });

      if (!response.ok) {
        throw new Error("Failed to create community");
      }

      setCommunityName("");
      setCommunityDescription("");
      onClose();
      alert("Community created successfully!");
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-container">
        <h2>Create a New Community</h2>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Community Name</label>
            <input
              type="text"
              value={communityName}
              onChange={(e) => setCommunityName(e.target.value)}
              placeholder="Enter community name"
              required
            />
          </div>
          <div className="form-group">
            <label>Community Description</label>
            <textarea
              value={communityDescription}
              onChange={(e) => setCommunityDescription(e.target.value)}
              placeholder="Enter description"
              required
            />
          </div>
          <button type="submit" className="submit-button" disabled={loading}>
            {loading ? "Creating..." : "Create Community"}
          </button>
        </form>
        <button onClick={onClose} className="close-modal-button">
          Close
        </button>
      </div>
    </div>
  );
};

export default CreateCommunityModal;
