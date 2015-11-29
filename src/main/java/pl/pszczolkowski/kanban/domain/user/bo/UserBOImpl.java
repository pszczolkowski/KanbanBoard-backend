package pl.pszczolkowski.kanban.domain.user.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.user.entity.User;
import pl.pszczolkowski.kanban.domain.user.repository.UserRepository;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;
import pl.pszczolkowski.kanban.service.user.PasswordEncodingService;
import pl.pszczolkowski.kanban.shared.annotations.BusinessObject;

@BusinessObject
public class UserBOImpl implements UserBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserBOImpl.class);

	private final UserRepository userRepository;
	private final PasswordEncodingService passwordEncodingService;

	@Autowired
	public UserBOImpl(UserRepository userRepository, PasswordEncodingService passwordEncodingService) {
		this.userRepository = userRepository;
		this.passwordEncodingService = passwordEncodingService;
	}

	@Override
	public UserSnapshot add(String login, String password, String username) {
		User user = userRepository.findOneByLoginIgnoreCase(login);
		if (user != null) {
			throw new IllegalArgumentException("User with login <" + login + "> already exists");
		}
		
		user = new User(login, passwordEncodingService.encode(password), username);
		user = userRepository.save(user);

		LOGGER.info("User <{}> created", login);

		return user.toSnapshot();
	}

}
