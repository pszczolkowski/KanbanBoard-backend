package pl.pszczolkowski.kanban.domain.user.snapshot;

public class UserSnapshot {

	private final Long id;
	private final String login;
	private final String password;
	private final String username;

	public UserSnapshot(Long id, String login, String password, String username) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

}
