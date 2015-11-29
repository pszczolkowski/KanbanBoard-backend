package pl.pszczolkowski.kanban.domain.user.bo;

import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

public interface UserBO {

	UserSnapshot add(String login, String password, String username);

}
