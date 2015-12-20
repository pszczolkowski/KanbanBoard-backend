package pl.pszczolkowski.kanban.web.restapi.label;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;

@ApiModel
public class Label {

	private final long id;
	private final long boardId;
	private final String name;
	private final String color;
	
	public Label(LabelSnapshot labelSnapshot) {
		this.id = labelSnapshot.getId();
		this.boardId = labelSnapshot.getBoardId();
		this.name = labelSnapshot.getName();
		this.color = labelSnapshot.getColor();
	}

	@ApiModelProperty("Unique identifier of label")
	public long getId() {
		return id;
	}

	@ApiModelProperty("Unique identifier of board that label belongs to")
	public long getBoardId() {
		return boardId;
	}

	@ApiModelProperty("Label name")
	public String getName() {
		return name;
	}

	@ApiModelProperty("Label color")
	public String getColor() {
		return color;
	}
	
}
