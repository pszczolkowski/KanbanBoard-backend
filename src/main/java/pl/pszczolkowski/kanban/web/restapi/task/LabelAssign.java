package pl.pszczolkowski.kanban.web.restapi.task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LabelAssign {

	@NotNull
	@Min(1)
	private Long taskId;
	
	@Min(1)
	private Long labelId;

	@ApiModelProperty(
		value = "Unique identifier of task that label should be assigned to",
		required = true)
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@ApiModelProperty(
		value = "Unique identifier of label that should be assigned to the task",
		required = false)
	public Long getLabelId() {
		return labelId;
	}

	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}
	
}
