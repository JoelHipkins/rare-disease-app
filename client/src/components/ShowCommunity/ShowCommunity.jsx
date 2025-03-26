import React, { useState } from "react";
import "./ShowCommunity.css";

const ShowCommunity = ({ community, token }) => {
  const [isJoined, setIsJoined] = useState(false);

  const handleJoinCommunity = () => {
    setIsJoined(true);

    alert("You have joined the community!");
  };

  return (
    <div className="community-card">
      <h3>{community.communityName}</h3>
      <p>Created by: {community.creatorName}</p>
      <p>Total Members: {community.totalMembers}</p>
      <button
        className={isJoined ? "joined-btn" : "join-btn"}
        onClick={handleJoinCommunity}
        disabled={isJoined}
      >
        {isJoined ? "Joined" : "Join Community"}
      </button>
    </div>
  );
};

export default ShowCommunity;
