package pl.pszczolkowski.kanban.web.restapi.board;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserPermissions {

	@NotNull
	@Min(1)
	private Long boardId;
	
	@NotNull
	@Min(1)
	private Long memberId;
	
	@NotNull
	private Permissions permissions;

	@ApiModelProperty(
		value = "Unique identifier of board",
		required = true)
	public Long getBoardId() {
		return boardId;
	}

	public void setBoardId(Long boardId) {
		this.boardId = boardId;
	}

	@ApiModelProperty(
		value = "Unique identifier of board member",
		required = true)
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@ApiModelProperty(
		value = "Permissions that should be set for the member",
		required = true)
	public Permissions getPermissions() {
		return permissions;
	}

	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}
	
}
