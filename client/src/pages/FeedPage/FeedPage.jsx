import { React, useState, useEffect } from "react";
import PostCreation from "../../components/CreatePost/PostCreation"; // Import PostCreation component
import ShowPost from "../../components/ShowPost/ShowPost"; // Import ShowPost component
import "./Feed.css";

export default function Feed() {
  const [posts, setPosts] = useState([]);

  const updatePosts = () => {
    fetch("http://localhost:8080/api/posts")
      .then((response) => response.json())
      .then((data) => setPosts(data))
      .catch((error) => console.error("Error fetching posts:", error));
  };

  useEffect(() => {
    updatePosts(); // Initially load the posts when component mounts
  }, []);

  return (
    <div className="feed-container">
      <PostCreation updatePosts={updatePosts} />{" "}
      {/* Pass the updatePosts handler to PostCreation */}
      <div className="feed-content">
        {posts.length === 0 ? (
          <p>No posts available.</p>
        ) : (
          posts.map((post) => (
            <ShowPost key={post.id} post={post} updatePost={updatePosts} /> // Pass the update function to ShowPost
          ))
        )}
      </div>
    </div>
  );
}
