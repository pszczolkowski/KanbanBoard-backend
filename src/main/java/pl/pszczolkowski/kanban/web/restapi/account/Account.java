package pl.pszczolkowski.kanban.web.restapi.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

@ApiModel
public class Account {

	private final Long id;
	private final String login;
	private final String username;

	public Account(UserSnapshot userSnapshot) {
		this.id = userSnapshot.getId();
		this.login = userSnapshot.getLogin();
		this.username = userSnapshot.getUsername();
	}

	@ApiModelProperty("Unique identifier of user")
	public Long getId() {
		return id;
	}

	@ApiModelProperty("User login")
	public String getLogin() {
		return login;
	}

	@ApiModelProperty("Username")
	public String getUsername() {
		return username;
	}

}
