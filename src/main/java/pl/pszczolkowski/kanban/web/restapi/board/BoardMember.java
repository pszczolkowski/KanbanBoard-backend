package pl.pszczolkowski.kanban.web.restapi.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardMemberSnapshot;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

@ApiModel
public class BoardMember {

	private final long id;
	private final long userId;
	private final String username;
	private final String login;
	private final Permissions permissions;
	
	public BoardMember(BoardMemberSnapshot boardMemberSnapshot, UserSnapshot userSnapshot) {
		this.id = boardMemberSnapshot.getId();
		this.userId = boardMemberSnapshot.getUserId();
		this.username = userSnapshot.getUsername();
		this.login = userSnapshot.getLogin();
		this.permissions = Permissions.from(boardMemberSnapshot.getPermissions());
	}

	@ApiModelProperty("Unique identifier of board member")
	public long getId() {
		return id;
	}

	@ApiModelProperty("Unique identifier of user")
	public long getUserId() {
		return userId;
	}

	@ApiModelProperty("Username")
	public String getUsername() {
		return username;
	}

	@ApiModelProperty("User login")
	public String getLogin() {
		return login;
	}

	@ApiModelProperty("User permissions to the board")
	public Permissions getPermissions() {
		return permissions;
	}
	
}
