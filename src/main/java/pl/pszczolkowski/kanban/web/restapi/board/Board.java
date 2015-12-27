package pl.pszczolkowski.kanban.web.restapi.board;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

@ApiModel()
public class Board {

	@JsonView(View.Summary.class)
	private final long id;
	
	@JsonView(View.Summary.class)
	private final String name;
	
	private List<BoardMember> members;
	
	public Board(BoardSnapshot boardSnapshot) {
		this.id = boardSnapshot.getId();
		this.name = boardSnapshot.getName();
	}
	
	public Board(BoardSnapshot boardSnapshot, Map<Long, UserSnapshot> userSnapshots) {
		this(boardSnapshot);
		this.members = boardSnapshot
				.getMembers()
				.stream()
				.map(member -> new BoardMember(member, userSnapshots.get(member.getUserId())))
				.collect(Collectors.toList());
	}

	@ApiModelProperty("Unique identifier of board")
	public long getId() {
		return id;
	}
	
	@ApiModelProperty("Board name")
	public String getName() {
		return name;
	}
	
	@ApiModelProperty("Board members")
	public List<BoardMember> getMembers() {
		return members;
	}
	
}
