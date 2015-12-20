CREATE TABLE board_column (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	board_id BIGINT NOT NULL CHECK (board_id >= 0),
	name VARCHAR(20) NOT NULL,
	position INT NOT NULL CHECK (position >= 0),
	version BIGINT NOT NULL,
	work_in_progress_limit INT NOT NULL CHECK (work_in_progress_limit >= 0)
	);