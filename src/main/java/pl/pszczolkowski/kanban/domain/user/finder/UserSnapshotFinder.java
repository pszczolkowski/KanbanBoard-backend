package pl.pszczolkowski.kanban.domain.user.finder;

import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

public interface UserSnapshotFinder {

	UserSnapshot findById(Long id);

	UserSnapshot findByLogin(String login);

}
