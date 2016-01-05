package pl.pszczolkowski.kanban.web.restapi.column;

import static pl.pszczolkowski.kanban.web.restapi.column.WorkInProgressLimitType.QUANTITY;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ColumnNew {

	@NotEmpty
	@Size(max = 30)
	private String name;
	
	@NotNull
	@Min(0)
	private Long boardId;
	
	@Min(1)
	private Integer workInProgressLimit;
	
	@NotNull
	private WorkInProgressLimitType workInProgressLimitType = QUANTITY;
	
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
	public Long getBoardId() {
		return boardId;
	}
	
	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	@ApiModelProperty(
		value = "Work in progress limit for the column",
		required = false)
	public Integer getWorkInProgressLimit() {
		return workInProgressLimit;
	}

	@ApiModelProperty(
		value = "Work in progress limit type for the column",
		required = false)
	public void setWorkInProgressLimit(Integer workInProgressLimit) {
		this.workInProgressLimit = workInProgressLimit;
	}

	public WorkInProgressLimitType getWorkInProgressLimitType() {
		return workInProgressLimitType;
	}

	public void setWorkInProgressLimitType(WorkInProgressLimitType workInProgressLimitType) {
		this.workInProgressLimitType = workInProgressLimitType;
	}
	
}
