import React, { useState, useEffect } from "react";
import "./ShowPost.css";

const ShowPost = ({ post }) => {
  const [updatedPost, setPost] = useState(post);
  const [isCommenting, setIsCommenting] = useState(false);
  const [commentContent, setCommentContent] = useState("");
  const [commentsList, setCommentsList] = useState([]);
  const [isAnswering, setIsAnswering] = useState(false);
  const [isParticipating, setIsParticipating] = useState(false);
  const [answerContent, setAnswerContent] = useState("");
  const [discussionContent, setDiscussionContent] = useState("");
  const [answersList, setAnswersList] = useState([]);
  const [participationsList, setParticipationsList] = useState([]);

  useEffect(() => {
    if (updatedPost.id) {
      fetch(`http://localhost:8080/api/posts/${updatedPost.id}/comments`)
        .then((response) => response.json())
        .then((data) => setCommentsList(data || []))
        .catch((error) => console.error("Error fetching comments:", error));

      if (updatedPost.type === "question") {
        fetch(`http://localhost:8080/api/posts/${updatedPost.id}/answers`)
          .then((response) => response.json())
          .then((data) => setAnswersList(data || []))
          .catch((error) => console.error("Error fetching answers:", error));
      }

      if (updatedPost.type === "discussion") {
        fetch(`http://localhost:8080/api/posts/${updatedPost.id}/discussions`)
          .then((response) => response.json())
          .then((data) => setParticipationsList(data || []))
          .catch((error) =>
            console.error("Error fetching participations:", error)
          );
      }
    }
  }, [updatedPost.id]);

  const calculateTimeAgo = (timestamp) => {
    const now = new Date();
    const postTime = new Date(timestamp);
    const diffInSeconds = (now - postTime) / 1000;
    const hours = Math.floor(diffInSeconds / 3600);
    return hours > 0 ? `${hours} hours ago` : "Just now";
  };

  const getProfileIcon = () => {
    return (
      <div
        style={{
          backgroundColor: "rgb(168, 212, 227)",
          fontWeight: "bold",
          color: "purple",
          borderRadius: "50%",
          width: "40px",
          height: "40px",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          fontSize: "20px",
        }}
      >
        {updatedPost.username.charAt(0).toUpperCase()}
      </div>
    );
  };

  const handleLike = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/posts/${updatedPost.id}/like`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + localStorage.getItem("jwt"),
          },
        }
      );
      const data = await response.json();
      if (response.status === 200) {
        setPost((prevPost) => ({
          ...prevPost,
          likesCount: prevPost.likesCount + 1,
        }));
      } else {
        console.error("Error:", data);
      }
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleComment = () => {
    setIsCommenting((prevState) => !prevState);
  };

  const handleSubmitComment = async () => {
    if (!commentContent.trim()) return;

    const token = localStorage.getItem("jwt");
    if (!token) {
      console.error("No valid JWT token found in localStorage.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/posts/${updatedPost.id}/comment`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({ content: commentContent }),
        }
      );

      if (response.status === 201) {
        setIsCommenting(false);
        setCommentContent("");
        setPost((prevPost) => ({
          ...prevPost,
          commentsCount: prevPost.commentsCount + 1,
        }));
        const newComment = { username: "You", content: commentContent };
        setCommentsList((prevComments) => [...prevComments, newComment]);
      }
    } catch (error) {
      console.error("Error submitting comment:", error);
    }
  };

  const handleAnswer = () => {
    setIsAnswering((prevState) => !prevState);
  };

  const handleSubmitAnswer = async () => {
    if (!answerContent.trim()) return;

    const token = localStorage.getItem("jwt");
    if (!token) {
      console.error("No valid JWT token found in localStorage.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/posts/${updatedPost.id}/answer`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
          body: JSON.stringify({ content: answerContent }),
        }
      );

      if (response.status === 201) {
        setPost((prevPost) => ({
          ...prevPost,
          answersCount: prevPost.answersCount + 1,
        }));
        setAnswersList((prevAnswers) => [
          ...prevAnswers,
          { username: "You", content: answerContent },
        ]);
        setIsAnswering(false);
        setAnswerContent("");
      }
    } catch (error) {
      console.error("Error submitting answer:", error);
    }
  };

  const handleParticipate = () => {
    setIsParticipating((prevState) => !prevState);
  };

  const handleSubmitParticipation = async () => {
    if (!discussionContent.trim()) return;

    const token = localStorage.getItem("jwt");
    if (!token) {
      console.error("No valid JWT token found in localStorage.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/posts/${updatedPost.id}/participate`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
          body: JSON.stringify({ content: discussionContent }),
        }
      );

      if (response.status === 201) {
        setPost((prevPost) => ({
          ...prevPost,
          participationsCount: prevPost.participationsCount + 1,
        }));
        setParticipationsList((prevParticipations) => [
          ...prevParticipations,
          { username: "You", content: discussionContent },
        ]);
        setIsParticipating(false);
        setDiscussionContent("");
      }
    } catch (error) {
      console.error("Error submitting participation:", error);
    }
  };
  return (
    <div className="post-card">
      <div className="post-header">
        {getProfileIcon()}
        <div className="post-username-time">
          <div className="username">{updatedPost.username}</div>
          <div className="time-ago">
            {calculateTimeAgo(updatedPost.createdAt)}
          </div>
        </div>
      </div>
      <div className="post-content">
        <p>{updatedPost.content}</p>
      </div>

      <div className="post-actions">
        {updatedPost.type === "text" && (
          <div>
            <button onClick={handleLike}>Like</button>
            <button onClick={handleComment}>Comment</button>
          </div>
        )}
        {updatedPost.type === "question" && (
          <button onClick={handleAnswer}>Answer</button>
        )}
        {updatedPost.type === "discussion" && (
          <button onClick={handleParticipate}>Participate</button>
        )}
      </div>

      <div className="post-stats">
        {updatedPost.type === "question" && (
          <span>{updatedPost.answersCount} Answers</span>
        )}
        {updatedPost.type === "discussion" && (
          <span>{updatedPost.participationsCount} Participants</span>
        )}
        {updatedPost.type === "text" && (
          <>
            <span>{updatedPost.likesCount} Likes</span> |
            <span>{updatedPost.commentsCount} Comments</span>
          </>
        )}
      </div>

      {isCommenting && (
        <div className="comments-section">
          <div className="comments-header">
            <h3>Comments</h3>
            {commentsList.map((comment, index) => (
              <div key={index} className="comment">
                <strong>{comment.username}</strong> {comment.content}
              </div>
            ))}
          </div>
          <div className="comment-input">
            <textarea
              value={commentContent}
              onChange={(e) => setCommentContent(e.target.value)}
              placeholder="Enter your comment..."
            ></textarea>
            <button onClick={handleSubmitComment}>Send</button>
          </div>
        </div>
      )}

      {isAnswering && (
        <div className="answers-section">
          <div className="answers-header">
            <h3>Answers</h3>
            {answersList.map((answer, index) => (
              <div key={index} className="answer">
                <strong>{answer.username}</strong>: {answer.content}
              </div>
            ))}
          </div>
          <div className="answer-input">
            <textarea
              value={answerContent}
              onChange={(e) => setAnswerContent(e.target.value)}
              placeholder="Enter your answer..."
            ></textarea>
            <button onClick={handleSubmitAnswer}>Send</button>
          </div>
        </div>
      )}

      {isParticipating && (
        <div className="participations-section">
          <div className="participations-header">
            <h3>Participants</h3>
            {participationsList.map((participation, index) => (
              <div key={index} className="participation">
                <strong>{participation.username}</strong>:{" "}
                {participation.content}
              </div>
            ))}
          </div>
          <div className="participation-input">
            <textarea
              value={discussionContent}
              onChange={(e) => setDiscussionContent(e.target.value)}
              placeholder="Enter your participation..."
            ></textarea>
            <button onClick={handleSubmitParticipation}>Send</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ShowPost;
