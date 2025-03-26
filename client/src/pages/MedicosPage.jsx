import React, { useState, useEffect } from "react";
import PostCreation from "../components/CreatePost/PostCreation"; // Import PostCreation component
import ShowPost from "../components/ShowPost/ShowPost"; // Import ShowPost component
// import { data } from "react-router-dom";

export default function MedicosPage() {
  const [posts, setPosts] = useState([]);

  const updatePosts = () => {
    fetch("http://localhost:8080/api/posts/medicos", {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt"), // Pass JWT token for authentication
      },
    })
      .then((response) => response.json())
      .then((data) => setPosts(data))

      .catch((error) => console.error("Error fetching posts:", error));
  };

  useEffect(() => {
    updatePosts(); // Initially load the posts when component mounts
  }, []);

  return (
    <div className="medicos-container">
      <h1 className="medicos-heading">Medical Uploads</h1>
      <div className="medicos-content">
        {posts.length === 0 ? (
          <p>No posts available from medical professionals.</p>
        ) : (
          posts.map((post) => (
            <ShowPost key={post.id} post={post} updatePost={updatePosts} /> // Pass the update function to ShowPost
          ))
        )}
      </div>
    </div>
  );
}
