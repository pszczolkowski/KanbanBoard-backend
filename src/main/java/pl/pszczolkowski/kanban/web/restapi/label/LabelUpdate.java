package pl.pszczolkowski.kanban.web.restapi.label;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LabelUpdate {

	@NotNull
	@Min(1)
	private Long id;
	
	@NotEmpty
	@Size(max = 100)
	private String name;
	
	@NotNull
	@Size(min = 7, max = 7)
	@Pattern(regexp = "^#[a-fA-F0-9]{6}$")
	private String color;

	@ApiModelProperty(
		value = "Unique identifier of label that should be updated",
		required = true)
	public Long getId() {
		return id;
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

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}
