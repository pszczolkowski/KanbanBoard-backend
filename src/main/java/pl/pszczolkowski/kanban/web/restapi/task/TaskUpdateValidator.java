package pl.pszczolkowski.kanban.web.restapi.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.label.finder.LabelSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.finder.TaskSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class TaskUpdateValidator extends AbstractValidator {

	private final TaskSnapshotFinder taskSnapshotFinder;
	private final BoardSnapshotFinder boardSnapshotFinder;
	private final LabelSnapshotFinder labelSnapshotFinder; 
	
	@Autowired
	public TaskUpdateValidator(TaskSnapshotFinder taskSnapshotFinder, BoardSnapshotFinder boardSnapshotFinder,
			LabelSnapshotFinder labelSnapshotFinder) {
		this.taskSnapshotFinder = taskSnapshotFinder;
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.labelSnapshotFinder = labelSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return TaskUpdate.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		TaskUpdate taskUpdate = (TaskUpdate) target;
		
		TaskSnapshot taskSnapshot = taskSnapshotFinder.findById(taskUpdate.getId());
		if (taskSnapshot == null) {
			errors.rejectValue("id", "TaskWithGivenIdDoesNotExist");
			return;
		}
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(taskSnapshot.getBoardId());
		if (boardSnapshot == null || loggedUserIsNotBoardMember(boardSnapshot)) {
			errors.rejectValue("id", "TaskWithGivenIdDoesNotExist");
			return;
		}
		
		if (taskUpdate.getLabelId() != null && labelSnapshotFinder.findByIdAndBoardId(taskUpdate.getLabelId(), taskSnapshot.getBoardId()) == null) {
			errors.rejectValue("labelId", "LabelWithGivenIdDoesNotExist");
			return;
		}
	}
	
	private boolean loggedUserIsNotBoardMember(BoardSnapshot boardSnapshot) {
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		
		return boardSnapshot
				.getMembers()
				.stream()
				.noneMatch(m -> m.getUserId() == loggedUserId);
	}
	
}
