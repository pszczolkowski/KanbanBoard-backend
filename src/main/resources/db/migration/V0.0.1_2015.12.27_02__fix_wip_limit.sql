ALTER TABLE board_column MODIFY COLUMN work_in_progress_limit INT;

ALTER TABLE board_column ADD CONSTRAINT CHECK (work_in_progress_limit >= 0);