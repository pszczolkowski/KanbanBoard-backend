CREATE TABLE board_history (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	board_id BIGINT NOT NULL CHECK (board_id >= 1),
	DATE TIMESTAMP NOT NULL
	);

CREATE TABLE board_history_column_sizes (
	board_history_id BIGINT NOT NULL,
	column_sizes INT,
	column_sizes_key VARCHAR(255),
	PRIMARY KEY (
		board_history_id,
		column_sizes_key
		)
	);

ALTER TABLE board_history_column_sizes ADD CONSTRAINT FK_board_history_column_sizes FOREIGN KEY (board_history_id) REFERENCES board_history(id);