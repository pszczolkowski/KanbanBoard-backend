package pl.pszczolkowski.kanban.web.restapi.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.label.finder.LabelSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.domain.user.finder.UserSnapshotFinder;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class TaskNewValidator extends AbstractValidator {

	private final BoardSnapshotFinder boardSnapshotFinder;
	private final ColumnSnapshotFinder columnSnapshotFinder;
	private final UserSnapshotFinder userSnapshotFinder;
	private final LabelSnapshotFinder labelSnapshotFinder; 
	
	@Autowired
	public TaskNewValidator(BoardSnapshotFinder boardSnapshotFinder, ColumnSnapshotFinder columnSnapshotFinder,
			UserSnapshotFinder userSnapshotFinder, LabelSnapshotFinder labelSnapshotFinder) {
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.columnSnapshotFinder = columnSnapshotFinder;
		this.userSnapshotFinder = userSnapshotFinder;
		this.labelSnapshotFinder = labelSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return TaskNew.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		TaskNew taskNew = (TaskNew) target;
		
		ColumnSnapshot columnSnapshot = columnSnapshotFinder.findById(taskNew.getColumnId());
		if (columnSnapshot == null) {
			errors.rejectValue("columnId", "ColumnWithGivenIdDoesntExist");
			return;
		}
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findByIdAndOwnerId(columnSnapshot.getBoardId(), LoggedUserService.getSnapshot().getId());
		if (boardSnapshot == null) {
			errors.rejectValue("columnId", "ColumnWithGivenIdDoesntExist");
			return;
		}
		
		if (taskNew.getAssigneeId() != null && userSnapshotFinder.findById(taskNew.getAssigneeId()) == null) {
			errors.rejectValue("assigneeId", "UserWithGivenIdDoesNotExist");
			return;
		}
		
		if (taskNew.getLabelId() != null && labelSnapshotFinder.findByIdAndBoardId(taskNew.getLabelId(), columnSnapshot.getBoardId()) == null) {
			errors.rejectValue("labelId", "LabelWithGivenIdDoesNotExist");
			return;
		}
	}
	
}
