package pl.pszczolkowski.kanban.domain.user.finder;

import java.util.Collection;
import java.util.Map;

import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

public interface UserSnapshotFinder {

	UserSnapshot findById(Long id);

	UserSnapshot findByLogin(String login);

	Map<Long, UserSnapshot> findAllAsMap(Collection<Long> memberIds);

}
