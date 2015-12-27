package pl.pszczolkowski.kanban.domain.user.finder;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.user.entity.User;
import pl.pszczolkowski.kanban.domain.user.repository.UserRepository;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.SnapshotFinder;

@SnapshotFinder
public class UserSnapshotFinderImpl implements UserSnapshotFinder {

	private final UserRepository userRepository;

	@Autowired
	public UserSnapshotFinderImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserSnapshot findById(Long id) {
		User user = userRepository.findOne(id);
		return user == null ? null : user.toSnapshot();
	}

	@Override
	public UserSnapshot findByLogin(String login) {
		User user = userRepository.findOneByLoginIgnoreCase(login);
		return user == null ? null : user.toSnapshot();
	}

	@Override
	public Map<Long, UserSnapshot> findAllAsMap(Collection<Long> memberIds) {
		return userRepository
			.findAll(memberIds)
			.stream()
			.map(User::toSnapshot)
			.collect(Collectors.toMap(user -> user.getId(), Function.identity()));
	}

}
