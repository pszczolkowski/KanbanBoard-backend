package pl.pszczolkowski.kanban.web.restapi.task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TaskMove {

	@NotNull
	@Min(1)
	private Long taskId;
	
	@Min(1)
	private Long columnId;
	
	@NotNull
	@Min(0)
	private Integer position;

	@ApiModelProperty(
		value = "Unique identifier of task",
		required = true)
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@ApiModelProperty(
		value = "Unique identifier of column that task should be moved to. "
				+ "If omitted, then task is move inside column it belongs to",
		required = false)
	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	@ApiModelProperty(
		value = "Position that task should be moved to",
		required = true)
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
}
