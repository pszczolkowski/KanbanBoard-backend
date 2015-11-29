package pl.pszczolkowski.kanban.service.user;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pl.pszczolkowski.kanban.domain.user.finder.UserSnapshotFinder;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

@Service
public class LoggedUserService implements ApplicationContextAware {

	private static UserSnapshotFinder userSnapshotFinder;
	
	public static UserSnapshot getSnapshot() {
		 if (userSnapshotFinder == null) {
	         throw new IllegalStateException("Required dependencies have not been injected yet");
	      }
		 
	      String login = SecurityContextHolder.getContext().getAuthentication().getName();
	      return userSnapshotFinder.findByLogin(login);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		userSnapshotFinder = applicationContext.getBean(UserSnapshotFinder.class);
	}
	
}
