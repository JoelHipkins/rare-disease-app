import React, { useEffect, useState } from "react";
import "./Pages.css";

const Feed = () => {
  const [posts, setPosts] = useState([]); // Store fetched posts

  useEffect(() => {
    // Fetch posts from the backend API
    fetch("http://localhost:8080/api/posts") // Adjust API endpoint as per your backend
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to fetch posts");
        }
        return res.json();
      })
      .then((data) => {
        setPosts(data); // Set fetched posts in state
      })
      .catch((error) => {
        console.error("Error fetching posts:", error);
      });
  }, []); // Empty dependency array, runs once on component mount

  return (
    <div className="feed-container">
      <h1>Welcome to the Feed Page</h1>
      <div className="feed-posts">
        {posts.length > 0 ? (
          posts.map((post) => (
            <div key={post.id} className="post">
              <h2>{post.title}</h2>
              <p>{post.content}</p>
            </div>
          ))
        ) : (
          <p>No posts available</p>
        )}
      </div>
    </div>
  );
};

export default Feed;
