package pl.pszczolkowski.kanban.web.restapi.task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserAssign {

	@NotNull
	@Min(1)
	private Long taskId;
	
	@Min(1)
	private Long assigneeId;

	@ApiModelProperty(
		value = "Unique identifier of task that user should be assigned to",
		required = true)
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@ApiModelProperty(
		value = "Unique identifier of user that should be assigned to the task",
		required = false)
	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}
	
}
