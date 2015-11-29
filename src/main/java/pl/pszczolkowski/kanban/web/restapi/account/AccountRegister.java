package pl.pszczolkowski.kanban.web.restapi.account;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class AccountRegister {

	@NotNull
	@Size(min = 3, max = 20)
	@Pattern(regexp = "^[a-z0-9]*$")
	private String login;

	@NotNull
	@Size(min = 5, max = 30)
	private String password;
	
	@NotNull
	@Size(min = 3, max = 20)
	private String username;

	@ApiModelProperty("User login")
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@ApiModelProperty("User password")
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@ApiModelProperty("Username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
