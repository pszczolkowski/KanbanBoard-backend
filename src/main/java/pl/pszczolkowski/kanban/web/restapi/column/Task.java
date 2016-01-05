package pl.pszczolkowski.kanban.web.restapi.column;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;

@ApiModel
public class Task {

	private final long id;
	private final int idOnBoard;
	private final long boardId;
	private final String title;
	private final String description;
	private final Long assigneeId;
	private final int position;
	private final Long labelId;
	private final TaskPriority priority;
	private final float size;
	private final LocalDateTime createdAt;
	private final long columnId;
	
	public Task(TaskSnapshot taskSnapshot) {
		this.id = taskSnapshot.getId();
		this.idOnBoard = taskSnapshot.getIdOnBoard();
		this.boardId = taskSnapshot.getBoardId();
		this.title = taskSnapshot.getTitle();
		this.description = taskSnapshot.getDescription();
		this.assigneeId = taskSnapshot.getAssigneeId();
		this.position = taskSnapshot.getPosition();
		this.labelId = taskSnapshot.getLabelId();
		this.priority = TaskPriority.from(taskSnapshot.getPriority());
		this.size = taskSnapshot.getSize();
		this.createdAt = taskSnapshot.getCreatedAt();
		this.columnId = taskSnapshot.getColumnId();
	}

	@ApiModelProperty("Unique identifier of task")
	public long getId() {
		return id;
	}

	@ApiModelProperty("Unique identifier of task on board")
	public int getIdOnBoard() {
		return idOnBoard;
	}

	@ApiModelProperty("Unique identifier of board that task belongs to")
	public long getBoardId() {
		return boardId;
	}

	@ApiModelProperty("Task title")
	public String getTitle() {
		return title;
	}

	@ApiModelProperty("Task description")
	public String getDescription() {
		return description;
	}

	@ApiModelProperty("Unique identifier of user that is assigned to the task")
	public Long getAssigneeId() {
		return assigneeId;
	}

	@ApiModelProperty("Task position in column")
	public int getPosition() {
		return position;
	}
	
	@ApiModelProperty("Unique identifier of label that is assigned to the task")
	public Long getLabelId() {
		return labelId;
	}
	
	@ApiModelProperty("Task priority")
	public TaskPriority getPriority() {
		return priority;
	}

	@ApiModelProperty("Task size")
	public float getSize() {
		return size;
	}
	
	@ApiModelProperty("Date of task creation")
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@ApiModelProperty("Unique identifier of column that task belongs to")
	public long getColumnId() {
		return columnId;
	}
	
}
