
CREATE TABLE post (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  authorId INTEGER,
  text TEXT,
  sticky BOOLEAN,
  topic VARCHAR(255),
  published BIGINT,
  ranking INTEGER
)