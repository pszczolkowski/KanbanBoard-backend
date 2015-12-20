package pl.pszczolkowski.kanban.builder;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import pl.pszczolkowski.kanban.domain.user.entity.User;
import pl.pszczolkowski.kanban.domain.user.repository.UserRepository;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

@Service
public class UserBuilder implements ApplicationContextAware {

	private static final String CLAZZ = UserBuilder.class.getSimpleName();
	private static final String password = repeat("*", 60);
	
	private static UserRepository userRepository;
	
	private String login = CLAZZ;
	private String username = CLAZZ;
	
	public UserBuilder withLogin(String login) {
		this.login = login;
		return this;
	}
	
	private static String repeat(String string, int n) {
		StringBuilder stringBuilder = new StringBuilder(string.length() * n);
		for(int i = 0; i < n; i++) {
			stringBuilder.append(string);
	    }
	    return stringBuilder.toString();
	}

	public UserBuilder withUsername(String username) {
		this.username = username;
		return this;
	}
	
	public UserSnapshot build() {
		if (userRepository == null) {
			throw new IllegalStateException("Required UserRepository dependency has not been initialized yet");
		}
		
		User user = new User(login, password, username);
		user = userRepository.save(user);
		
		return user.toSnapshot();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		userRepository = applicationContext.getBean(UserRepository.class);
	}
	
	public static UserBuilder anUser() {
		return new UserBuilder();
	}
	
}
