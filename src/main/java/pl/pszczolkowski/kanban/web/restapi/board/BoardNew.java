package pl.pszczolkowski.kanban.web.restapi.board;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BoardNew {

	@NotNull
	@Size(min = 3, max = 50)
	private String name;

	@ApiModelProperty(
		value = "Name for board",
		required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
