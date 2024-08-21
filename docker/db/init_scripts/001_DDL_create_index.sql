USE board;
-- user 테이블
CREATE INDEX idx_user_userid ON user (userId);
CREATE INDEX idx_user_nickname ON user (nickname);
CREATE INDEX idx_user_status ON user (status);

-- category 테이블
CREATE INDEX idx_category_name ON category (name);

-- post 테이블
CREATE INDEX idx_post_name ON post (name);
CREATE INDEX idx_post_createtime ON post (createTime);
CREATE INDEX idx_post_views ON post (views);
CREATE INDEX idx_post_category ON post (categoryId);
CREATE INDEX idx_post_user ON post (userId);

-- file 테이블
CREATE INDEX idx_file_name ON file (name);
CREATE INDEX idx_file_post ON file (postId);
œ
-- comment 테이블
CREATE INDEX idx_comment_post ON comment (postId);
CREATE INDEX idx_comment_subcomment ON comment (subCommentId);

-- tag 테이블
CREATE INDEX idx_tag_name ON tag (name);
CREATE INDEX idx_tag_url ON tag (url);

-- posttag 테이블
CREATE INDEX idx_posttag_post ON posttag (postId);
CREATE INDEX idx_posttag_tag ON posttag (tagId);