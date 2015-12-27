package pl.pszczolkowski.kanban.web.restapi.column;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ColumnDelete {

	@NotNull
	@Min(1)
	private Long columnId;
	
	@Min(1)
	private Long columnToMove;

	@ApiModelProperty(
		value = "Unique identifier of column that should be deleted",
		required = true)
	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	@ApiModelProperty(
		value = "Unique identifier of column that tasks from deleted column should be moved to",
		required = false)
	public Long getColumnToMove() {
		return columnToMove;
	}

	public void setColumnToMove(Long columnToMove) {
		this.columnToMove = columnToMove;
	}
	
}
