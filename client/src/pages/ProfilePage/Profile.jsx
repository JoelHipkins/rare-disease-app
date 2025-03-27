import React, { useState, useEffect } from "react";
import "./Profile.css";

const ProfilePage = () => {
  const [user, setUser] = useState(null);
  const [isSettingsOpen, setIsSettingsOpen] = useState(false); // Controls the visibility of settings options
  const [isEditing, setIsEditing] = useState(false); // Controls the editing form visibility
  const [newUsername, setNewUsername] = useState("");
  const [newRole, setNewRole] = useState("");
  const [isDeleteConfirmationOpen, setIsDeleteConfirmationOpen] =
    useState(false); // Controls the delete confirmation popup

  useEffect(() => {
    const token = localStorage.getItem("jwt");
    fetch("http://localhost:8080/api/users/profile", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setUser(data);
        setNewUsername(data.username);
        setNewRole(data.role);
      })
      .catch((error) => console.error("Error fetching user profile:", error));
  }, []);

  const handleUpdateClick = () => {
    // Update the profile in the backend
    const token = localStorage.getItem("jwt");
    const updatedUser = { username: newUsername, role: newRole };

    fetch("http://localhost:8080/api/users/profile", {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updatedUser),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("Profile updated:", data);
        setUser(data); // Update the user state with the new profile data
        setIsEditing(false); // Hide the update form after updating
        setIsSettingsOpen(false); // Close the settings options
      })
      .catch((error) => console.error("Error updating user profile:", error));
  };

  const handleCancelClick = () => {
    setIsEditing(false); // Close the edit form without saving changes
  };

  const handleRoleChange = (e) => setNewRole(e.target.value);
  const handleUsernameChange = (e) => setNewUsername(e.target.value);

  const toggleSettings = () => {
    setIsSettingsOpen(!isSettingsOpen); // Toggle the settings options
  };

  const handleUpdateToggle = () => {
    setIsEditing(!isEditing);
  };

  const handleDeleteProfile = () => {
    // Display the delete confirmation popup
    setIsDeleteConfirmationOpen(true);
  };

  const handleDeleteConfirmation = (confirm) => {
    if (confirm) {
      const token = localStorage.getItem("jwt");
      fetch("http://localhost:8080/api/users/delete", {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
        .then((response) => {
          if (response.status === 200) {
            return response.json();
          } else {
            throw new Error("Failed to delete user");
          }
        })
        .then((data) => {
          console.log("Profile deleted:", data);
          localStorage.removeItem("jwt"); // Remove the JWT token
          window.location.href = "http://localhost:5173"; // Redirect to the login page
        })
        .catch((error) => {
          console.error("Error deleting user profile:", error);
        });
    }
    // Close the delete confirmation modal
    setIsDeleteConfirmationOpen(false);
  };

  if (!user) {
    return <div>Loading...</div>;
  }

  return (
    <div className="profile-container">
      <div className="profile-header">
        <div className="profile-icon">
          {user.username.charAt(0).toUpperCase()}
        </div>
        <div className="profile-info">
          <h2>{user.username}</h2>
          <p>{user.role}</p>
        </div>
        <button className="settings-button" onClick={toggleSettings}>
          ⚙️
        </button>
      </div>

      {isSettingsOpen && (
        <div className="profile-settings-actions">
          <button onClick={handleUpdateToggle}>Update Profile</button>
          <button onClick={handleDeleteProfile}>Delete Profile</button>
        </div>
      )}

      {isEditing && (
        <div className="profile-update-form">
          <input
            type="text"
            placeholder="Enter new username"
            value={newUsername}
            onChange={handleUsernameChange}
          />
          <select value={newRole} onChange={handleRoleChange}>
            <option value="doctor">Doctor</option>
            <option value="pharma">Pharma</option>
            <option value="patient">Patient</option>
          </select>
          <div className="profile-actions">
            <button onClick={handleUpdateClick}>Update</button>
            <button onClick={handleCancelClick}>Close</button>
          </div>
        </div>
      )}

      {/* Delete Confirmation Popup */}
      {isDeleteConfirmationOpen && (
        <div className="delete-confirmation-modal">
          <p>
            Are you sure you want to delete your profile? All your data will be
            deleted.
          </p>
          <button onClick={() => handleDeleteConfirmation(true)}>
            Yes, Delete
          </button>
          <button onClick={() => handleDeleteConfirmation(false)}>
            No, Close
          </button>
        </div>
      )}
    </div>
  );
};

export default ProfilePage;
