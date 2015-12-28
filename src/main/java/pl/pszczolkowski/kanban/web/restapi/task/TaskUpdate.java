package pl.pszczolkowski.kanban.web.restapi.task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TaskUpdate {

	@NotNull
	@Min(1)
	private Long id; 
	
	@NotNull
	@Size(min = 2, max = 500)
	private String title;
	
	@Size(min = 1, max = 10000)
	private String description;
	
	@Min(1)
	private Long labelId;
	
	@NotNull
	private TaskPriority priority;


	@ApiModelProperty("Unique identifier of task")
	public Long getId() {
		return id;
	}
	
	@ApiModelProperty("Title for task")
	public String getTitle() {
		return title;
	}

	@ApiModelProperty("Description for task")
	public String getDescription() {
		return description;
	}

	@ApiModelProperty("Unique identifier of label that should be assigned to task")
	public Long getLabelId() {
		return labelId;
	}
	
	@ApiModelProperty("Priority that should be set for task")
	public TaskPriority getPriority() {
		return priority;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}
	
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	
}
