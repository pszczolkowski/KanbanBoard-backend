package pl.pszczolkowski.kanban.web.restapi.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.label.finder.LabelSnapshotFinder;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.domain.task.finder.TaskSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class LabelAssignValidator extends AbstractValidator {

	private final TaskSnapshotFinder taskSnapshotFinder;
	private final BoardSnapshotFinder boardSnapshotFinder;
	private final LabelSnapshotFinder labelSnapshotFinder; 
	
	@Autowired
	public LabelAssignValidator(TaskSnapshotFinder taskSnapshotFinder, BoardSnapshotFinder boardSnapshotFinder,
			LabelSnapshotFinder labelSnapshotFinder) {
		this.taskSnapshotFinder = taskSnapshotFinder;
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.labelSnapshotFinder = labelSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return LabelAssign.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		LabelAssign labelAssign = (LabelAssign) target;
		
		TaskSnapshot taskSnapshot = taskSnapshotFinder.findById(labelAssign.getTaskId());
		if (taskSnapshot == null) {
			errors.rejectValue("taskId", "TaskWithGivenIdDoesNotExist");
			return;
		}
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(taskSnapshot.getBoardId());
		if (loggedUserIsNotBoardMember(boardSnapshot)) {
			errors.rejectValue("taskId", "TaskWithGivenIdDoesNotExist");
			return;
		}
		
		if (labelAssign.getLabelId() != null) {
			LabelSnapshot labelSnapshot = labelSnapshotFinder.findByIdAndBoardId(labelAssign.getLabelId(), taskSnapshot.getBoardId());
			if (labelSnapshot == null) {
				errors.rejectValue("labelId", "LabelWithGivenIdDoesNotExist");
				return;
			}
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
