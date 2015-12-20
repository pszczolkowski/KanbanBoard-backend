package pl.pszczolkowski.kanban.web.restapi.task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ColumnNew {

	@NotEmpty
	@Size(max = 20)
	private String name;
	
	@NotNull
	@Min(0)
	private long boardId;
	
	@Min(0)
	private int workInProgressLimit;
	
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
		value = "Unique identifier of board that column should be assigned to",
		required = true)
	public long getBoardId() {
		return boardId;
	}
	
	public void setBoardId(long boardId) {
		this.boardId = boardId;
	}

	@ApiModelProperty(
		value = "Work in progress limit for the column",
		required = false)
	public int getWorkInProgressLimit() {
		return workInProgressLimit;
	}

	public void setWorkInProgressLimit(int workInProgressLimit) {
		this.workInProgressLimit = workInProgressLimit;
	}
	
}
