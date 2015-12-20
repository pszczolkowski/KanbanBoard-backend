package pl.pszczolkowski.kanban.web.restapi.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;

@ApiModel()
public class Board {

	private final long id;
	private final String name;
	
	public Board(BoardSnapshot boardSnapshot) {
		this.id = boardSnapshot.getId();
		this.name = boardSnapshot.getName();
	}
	
	@ApiModelProperty("Unique identifier of board")
	public long getId() {
		return id;
	}
	
	@ApiModelProperty("Board name")
	public String getName() {
		return name;
	}
	
}
