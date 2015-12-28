package pl.pszczolkowski.kanban.web.restapi.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.finder.TaskSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.domain.user.finder.UserSnapshotFinder;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class UserAssignValidator extends AbstractValidator {

	private final TaskSnapshotFinder taskSnapshotFinder;
	private final BoardSnapshotFinder boardSnapshotFinder;
	private final UserSnapshotFinder userSnapshotFinder;
	
	@Autowired
	public UserAssignValidator(TaskSnapshotFinder taskSnapshotFinder, BoardSnapshotFinder boardSnapshotFinder,
			UserSnapshotFinder userSnapshotFinder) {
		this.taskSnapshotFinder = taskSnapshotFinder;
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.userSnapshotFinder = userSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserAssign.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		UserAssign userAssign = (UserAssign) target;
		
		TaskSnapshot taskSnapshot = taskSnapshotFinder.findById(userAssign.getTaskId());
		if (taskSnapshot == null) {
			errors.rejectValue("taskId", "TaskWithGivenIdDoesNotExist");
			return;
		}
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(taskSnapshot.getBoardId());
		UserSnapshot loggedUserSnapshot = LoggedUserService.getSnapshot();
		if (userIsNotBoardMember(loggedUserSnapshot, boardSnapshot)) {
			errors.rejectValue("taskId", "TaskWithGivenIdDoesNotExist");
			return;
		}
		
		if (userAssign.getAssigneeId() != null) {
			UserSnapshot userSnapshot = userSnapshotFinder.findById(userAssign.getAssigneeId());
			if (userSnapshot == null || userIsNotBoardMember(userSnapshot, boardSnapshot)) {
				errors.rejectValue("taskId", "UserWithGivenIdDoesNotExist");
				return;
			}
		}
	}

	private boolean userIsNotBoardMember(UserSnapshot userSnapshot, BoardSnapshot boardSnapshot) {
		return boardSnapshot
				.getMembers()
				.stream()
				.noneMatch(m -> m.getUserId() == userSnapshot.getId());
	}
	
}
