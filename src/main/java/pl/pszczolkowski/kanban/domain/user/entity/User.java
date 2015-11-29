package pl.pszczolkowski.kanban.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;
import pl.pszczolkowski.kanban.shared.exception.EntityInStateNewException;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9]*$")
	@Size(min = 3, max = 20)
	@Column(unique = true)
	private String login;

	@NotNull
	@Size(min = 60, max = 60)
	private String password;

	@NotNull
	@Size(min = 3, max = 20)
	private String username;

	protected User() {}

	public User(String login, String password, String username) {
		this.login = login.toLowerCase();
		this.password = password;
		this.username = username;
	}

	public UserSnapshot toSnapshot() {
		if (id == null) {
			throw new EntityInStateNewException();
		}

		return new UserSnapshot(id, login, password, username);
	}
}
