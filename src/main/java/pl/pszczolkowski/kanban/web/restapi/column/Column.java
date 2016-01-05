package pl.pszczolkowski.kanban.web.restapi.column;

import static java.util.stream.Collectors.toList;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;

@ApiModel
public class Column {

	private final long id;
	private final String name;
	private final long boardId;
	private final int position;
	private final Integer workInProgressLimit;
	private final WorkInProgressLimitType workInProgressLimitType;
	private final List<Task> tasks;
	
	public Column(ColumnSnapshot columnSnapshot) {
		this.id = columnSnapshot.getId();
		this.name = columnSnapshot.getName();
		this.boardId = columnSnapshot.getBoardId();
		this.position = columnSnapshot.getPosition();
		this.workInProgressLimit = columnSnapshot.getWorkInProgressLimit();
		this.workInProgressLimitType = WorkInProgressLimitType.from(columnSnapshot.getWorkInProgressLimitType());
		this.tasks = columnSnapshot
				.getTasks()
				.stream()
				.map(Task::new)
				.collect(toList());
	}

	@ApiModelProperty("Unique identifier of column")
	public long getId() {
		return id;
	}
	
	@ApiModelProperty("Column name")
	public String getName() {
		return name;
	}

	@ApiModelProperty("Unique identifier of board that column is assigned to")
	public long getBoardId() {
		return boardId;
	}

	@ApiModelProperty("Position of column in board")
	public int getPosition() {
		return position;
	}

	@ApiModelProperty("Work in progress limit for column")
	public Integer getWorkInProgressLimit() {
		return workInProgressLimit;
	}
	
	@ApiModelProperty("Work in progress limit type for column")
	public WorkInProgressLimitType getWorkInProgressLimitType() {
		return workInProgressLimitType;
	}

	@ApiModelProperty("Tasks assigned to the column")
	public List<Task> getTasks() {
		return tasks;
	}
	
}
