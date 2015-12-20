CREATE TABLE task (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	assignee_id BIGINT CHECK (assignee_id >= 1),
	board_id BIGINT NOT NULL CHECK (board_id >= 1),
	created_at TIMESTAMP NOT NULL,
	description TEXT(10000),
	id_on_board INT NOT NULL CHECK (id_on_board >= 1),
	position INT NOT NULL CHECK (position >= 0),
	title VARCHAR(500) NOT NULL,
	version BIGINT NOT NULL,
	column_id BIGINT NOT NULL
	);

ALTER TABLE task ADD CONSTRAINT FK_task FOREIGN KEY (column_id) REFERENCES board_column(id);
