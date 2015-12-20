package pl.pszczolkowski.kanban.web.restapi.label;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LabelNew {

	@NotNull
	@Min(1)
	private long boardId;
	
	@NotEmpty
	@Size(max = 100)
	private String name;
	
	@NotNull
	@Size(min = 7, max = 7)
	@Pattern(regexp = "^#[a-fA-F0-9]{6}$")
	private String color;

	@ApiModelProperty(
		value = "Unique identifier of board that label should belong to",
		required = true)
	public long getBoardId() {
		return boardId;
	}

	@ApiModelProperty(
		value = "Name for label",
		required = true)
	public String getName() {
		return name;
	}

	@ApiModelProperty(
		value = "Color for label",
		required = true)
	public String getColor() {
		return color;
	}
	
}
