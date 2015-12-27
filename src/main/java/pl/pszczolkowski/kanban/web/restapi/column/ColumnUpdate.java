package pl.pszczolkowski.kanban.web.restapi.column;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ColumnUpdate {

	@NotNull
	@Min(1)
	private Long id;
	
	@NotEmpty
	@Size(max = 30)
	private String name;
	
	@Min(1)
	private Integer workInProgressLimit;

	@ApiModelProperty(
		value = "Unique identifier of column that should be updated",
		required = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ApiModelProperty(
		value = "Name for column",
		required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(
		value = "Work in progress limit for column",
		required = true)
	public Integer getWorkInProgressLimit() {
		return workInProgressLimit;
	}

	public void setWorkInProgressLimit(Integer workInProgressLimit) {
		this.workInProgressLimit = workInProgressLimit;
	}
	
}
