DROP DATABASE IF EXISTS board;
CREATE DATABASE board CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE board;

CREATE TABLE user (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      userId VARCHAR(45) NOT NULL,
                      password VARCHAR(64) NOT NULL,
                      nickname VARCHAR(45) NOT NULL,
                      isAdmin TINYINT(1) DEFAULT 0,
                      createTime DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      isWithdraw TINYINT(1) DEFAULT 0,
                      status ENUM('DEFAULT', 'ADMIN', 'DELTED') DEFAULT 'DEFAULT'
);

CREATE TABLE category (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(45) NOT NULL
);

CREATE TABLE post (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(45) NOT NULL,
                      isAdmin TINYINT(1) DEFAULT 0,
                      contents VARCHAR(500) NOT NULL,
                      createTime DATETIME DEFAULT CURRENT_TIMESTAMP,
                      views INT DEFAULT 0,
                      categoryId INT,
                      userId INT,
                      FOREIGN KEY (categoryId) REFERENCES category(id),
                      FOREIGN KEY (userId) REFERENCES user(id)
);

CREATE TABLE file (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      path VARCHAR(108) NOT NULL,
                      name VARCHAR(45) NOT NULL,
                      extension VARCHAR(45) NOT NULL,
                      postId INT,
                      FOREIGN KEY (postId) REFERENCES post(id)
);

CREATE TABLE comment (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         postId INT,
                         contents VARCHAR(300) NOT NULL,
                         subCommentId INT,
                         FOREIGN KEY (postId) REFERENCES post(id)
);

CREATE TABLE tag (
                     id INT PRIMARY KEY AUTO_INCREMENT,
                     name VARCHAR(45) NOT NULL,
                     url VARCHAR(45) NOT NULL
);

CREATE TABLE posttag (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         postId INT,
                         tagId INT,
                         FOREIGN KEY (postId) REFERENCES post(id),
                         FOREIGN KEY (tagId) REFERENCES tag(id)
);
