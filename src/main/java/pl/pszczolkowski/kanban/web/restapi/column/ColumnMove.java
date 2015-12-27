package pl.pszczolkowski.kanban.web.restapi.column;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ColumnMove {

	@NotNull
	@Min(1)
	private Long columnId;
	
	@NotNull
	@Min(0)
	private Integer position;

	@ApiModelProperty(
		value = "Unique identifier of column",
		required = true)
	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	@ApiModelProperty(
		value = "Position that column should be moved to",
		required = true)
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
}
