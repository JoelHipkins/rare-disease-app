import React, { useState } from "react";
import "./PostCreation.css";
import { jwtDecode } from "jwt-decode";

const PostCreation = ({ updatePosts }) => {
  const [postType, setPostType] = useState("text");
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const token = localStorage.getItem("jwt");
  let userName = "Guest"; // Default to 'Guest' if no token is found

  if (token) {
    try {
      const decodedToken = jwtDecode(token);
      userName = decodedToken.username || "Guest"; // Assuming username is part of the token payload
    } catch (error) {
      console.error("Error decoding token:", error);
    }
  }

  const userInitial = userName.charAt(0).toUpperCase();

  const handleContentChange = (e) => {
    setContent(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Prevent empty posts from being submitted
    if (!content.trim()) {
      setError("Post content cannot be empty");
      return;
    }

    setLoading(true);
    setError("");

    if (!token) {
      setError("User is not authenticated. Please log in.");
      setLoading(false);
      return;
    }

    const postData = {
      content,
      type: postType,
    };

    try {
      const response = await fetch("http://localhost:8080/api/posts", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(postData),
      });

      const data = await response.text();

      if (response.status === 201) {
        alert("Post created successfully!");
        setContent("");
        updatePosts(); // Update posts in Feed.jsx
      } else {
        setError(data || "An error occurred.");
      }
    } catch (error) {
      console.error("Error submitting post:", error);
      setError("An error occurred while creating the post.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="post-creation-container">
      <div className="post-header">
        <div className="user-avatar-circle">{userInitial}</div>
        <input
          type="text"
          placeholder="What's on your mind?"
          className="post-input"
          value={content}
          onChange={handleContentChange}
        />
      </div>

      <div className="post-options">
        <button
          onClick={() => setPostType("text")}
          className={`post-option ${postType === "text" ? "active" : ""}`}
        >
          Text
        </button>
        <button
          onClick={() => setPostType("question")}
          className={`post-option ${postType === "question" ? "active" : ""}`}
        >
          Question
        </button>
        <button
          onClick={() => setPostType("discussion")}
          className={`post-option ${postType === "discussion" ? "active" : ""}`}
        >
          Discussion
        </button>
      </div>

      <button className="post-submit" onClick={handleSubmit} disabled={loading}>
        {loading ? "Posting..." : "Post"}
      </button>

      {error && <p className="error-message">{error}</p>}
    </div>
  );
};

export default PostCreation;
