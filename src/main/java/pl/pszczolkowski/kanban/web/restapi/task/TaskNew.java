package pl.pszczolkowski.kanban.web.restapi.task;

import static pl.pszczolkowski.kanban.web.restapi.task.TaskPriority.LOW;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TaskNew {

	@NotNull
	@Size(min = 2, max = 500)
	private String title;
	
	@Size(min = 1, max = 10000)
	private String description;
	
	@Min(1)
	private Long assigneeId;
	
	@NotNull
	@Min(1)
	private long columnId;
	
	@Min(1)
	private Long labelId;
	
	private TaskPriority priority = LOW;

	@ApiModelProperty("Title for task")
	public String getTitle() {
		return title;
	}

	@ApiModelProperty("Description for task")
	public String getDescription() {
		return description;
	}

	@ApiModelProperty("Unique identifier of user that should be assigned to the task")
	public Long getAssigneeId() {
		return assigneeId;
	}

	@ApiModelProperty("Priority that should be set for task")
	public TaskPriority getPriority() {
		return priority;
	}
	
	@ApiModelProperty("Unique identifier of column that task should be assigned to")
	public long getColumnId() {
		return columnId;
	}
	
	@ApiModelProperty("Unique identifier of label that should be assigned to task")
	public Long getLabelId() {
		return labelId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}

	public void setColumnId(long columnId) {
		this.columnId = columnId;
	}
	
	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}
	
}
