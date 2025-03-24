drop database if exists rare_disease_app;
create database rare_disease_app;
use rare_disease_app;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE likes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,              -- The user who liked the post
    post_id INT NOT NULL,              -- The post that was liked (must be a text-type post)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Time of like
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (post_id) REFERENCES posts(id),
    UNIQUE(user_id, post_id)  -- Prevents duplicate likes on the same post
);


CREATE TABLE comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,              -- Foreign key to posts table
    user_id INT NOT NULL,              -- Foreign key to users table
    content TEXT NOT NULL,             -- Content of the comment
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id),  -- Link to the post being commented on
    FOREIGN KEY (user_id) REFERENCES users(id)   -- Link to the user who made the comment
);

CREATE TABLE answers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,              -- Foreign key to posts table (this would be a question post)
    user_id INT NOT NULL,              -- Foreign key to users table
    content TEXT NOT NULL,             -- Content of the answer
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id),  -- Link to the question post
    FOREIGN KEY (user_id) REFERENCES users(id)   -- Link to the user who provided the answer
);

CREATE TABLE discussions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,              -- Foreign key to posts table (this would be a discussion post)
    user_id INT NOT NULL,              -- Foreign key to users table
    content TEXT NOT NULL,             -- Content of the participation in the discussion
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id),  -- Link to the discussion post
    FOREIGN KEY (user_id) REFERENCES users(id)   -- Link to the user who participated
);


-- Insert some sample users into the users table (you can modify or remove this part as needed)
INSERT INTO users (username, email, password, role)
VALUES
    ('testuser1', 'testuser1@example.com', 'password1', 'admin'),
    ('testuser2', 'testuser2@example.com', 'password2', 'user');