import React, { useState, useEffect } from "react";
import "./CommunityPage.css"; // You can style your page here
import CreateCommunityModal from "./CreateCommunityModal";
import ShowCommunity from "../../components/ShowCommunity/ShowCommunity";
ShowCommunity; // Import the ShowCommunity component

const CommunityPage = () => {
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [communities, setCommunities] = useState([]); // Add state to store communities

  const token = localStorage.getItem("jwt"); // Get JWT token for authenticated actions

  useEffect(() => {
    fetch("http://localhost:8080/api/communities")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        console.log(data); // Log the response here
        if (Array.isArray(data)) {
          setCommunities(data); // Only set if it's an array
        } else {
          console.error("Error: Expected an array of communities", data);
        }
      })
      .catch((error) => {
        console.error("Error fetching communities:", error);
      });
  }, []);

  const handleCreateCommunity = () => {
    setShowCreateModal(true);
  };

  const handleCloseModal = () => {
    setShowCreateModal(false);
  };

  return (
    <div className="community-page-container">
      {/* Header with title and buttons */}
      <div className="community-page-header">
        <h2>Communities</h2>
        <div className="community-header-actions">
          <button className="joined-button">Joined Communities</button>
          <button
            className="create-community-button"
            onClick={handleCreateCommunity}
          >
            Create One
          </button>
        </div>
      </div>

      {/* Conditional rendering of the Create Community modal */}
      {showCreateModal && <CreateCommunityModal onClose={handleCloseModal} />}

      {/* Communities display section */}
      <div className="community-cards-container">
        {communities.length === 0 ? (
          <p>No communities available.</p>
        ) : (
          communities.map((community) => (
            <div key={community.communityId} className="community-card">
              <ShowCommunity community={community} token={token} />
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default CommunityPage;
