package pl.pszczolkowski.kanban.web.restapi.board;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class UserInvitation {

	@NotNull
	@Min(1)
	private Long boardId;
	
	@NotNull
	@Size(min = 3)
	private String login;

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
		value = "User login",
		required = true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
}
