package pl.pszczolkowski.kanban.domain.task.snapshot;

import java.time.LocalDateTime;

import pl.pszczolkowski.kanban.domain.task.entity.TaskPriority;

public class TaskSnapshot {

	private final long id;
	private final int idOnBoard;
	private final long boardId;
	private final String title;
	private final String description;
	private final Long assigneeId;
	private final int position;
	private final Long labelId;
	private final TaskPriority priority;
	private final LocalDateTime createdAt;
	private final long columnId;
	
	public TaskSnapshot(long id, int idOnBoard, long boardId, String title, String description, Long assigneeId,
			int position, Long labelId, TaskPriority priority, LocalDateTime createdAt, long columnId) {
		this.id = id;
		this.idOnBoard = idOnBoard;
		this.boardId = boardId;
		this.title = title;
		this.description = description;
		this.assigneeId = assigneeId;
		this.position = position;
		this.labelId = labelId;
		this.priority = priority;
		this.createdAt = createdAt;
		this.columnId = columnId;
	}

	public long getId() {
		return id;
	}

	public int getIdOnBoard() {
		return idOnBoard;
	}

	public long getBoardId() {
		return boardId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Long getAssigneeId() {
		return assigneeId;
	}

	public int getPosition() {
		return position;
	}
	
	public Long getLabelId() {
		return labelId;
	}
	
	public TaskPriority getPriority() {
		return priority;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public long getColumnId() {
		return columnId;
	}
	
}
