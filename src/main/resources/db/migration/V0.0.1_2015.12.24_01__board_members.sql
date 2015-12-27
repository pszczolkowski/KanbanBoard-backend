CREATE TABLE board_member (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	permissions VARCHAR(255) NOT NULL,
	user_id BIGINT NOT NULL CHECK (user_id >= 1),
	board_id BIGINT NOT NULL,
	version BIGINT NOT NULL
	);

ALTER TABLE board_member ADD CONSTRAINT FK_board_member FOREIGN KEY (board_id) REFERENCES board (id);
ALTER TABLE board_member ADD CONSTRAINT UK_board_member UNIQUE (user_id, board_id);

ALTER TABLE board DROP COLUMN owner_id;