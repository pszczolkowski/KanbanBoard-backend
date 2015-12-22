package pl.pszczolkowski.kanban.web.restapi.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.finder.TaskSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class TaskMoveValidator extends AbstractValidator {

	private final TaskSnapshotFinder taskSnapshotFinder;
	private final BoardSnapshotFinder boardSnapshotFinder;
	private final ColumnSnapshotFinder columnSnapshotFinder;

	@Autowired
	public TaskMoveValidator(TaskSnapshotFinder taskSnapshotFinder, BoardSnapshotFinder boardSnapshotFinder,
			ColumnSnapshotFinder columnSnapshotFinder) {
		this.taskSnapshotFinder = taskSnapshotFinder;
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.columnSnapshotFinder = columnSnapshotFinder;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return TaskMove.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		TaskMove taskMove = (TaskMove) target;

		TaskSnapshot taskSnapshot = taskSnapshotFinder.findById(taskMove.getTaskId());
		if (taskSnapshot == null) {
			errors.rejectValue("taskId", "TaskWithGivenIdDoesNotExist");
			return;
		}
		
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findByIdAndOwnerId(taskSnapshot.getBoardId(), loggedUserId);
		if (boardSnapshot == null) {
			errors.rejectValue("taskId", "TaskWithGivenIdDoesNotExist");
			return;
		}
		
		if (taskMove.getColumnId() != null) {
			ColumnSnapshot columnSnapshot = columnSnapshotFinder.findById(taskMove.getColumnId());
			if (columnSnapshot == null || columnSnapshot.getBoardId() != boardSnapshot.getId()) {
				errors.rejectValue("columnId", "ColumnWithGivenIdDoesntExist");
				return;
			}
			
			if (taskMove.getPosition() > columnSnapshot.getTasks().size()) {
				errors.rejectValue("position", "InvalidPosition");
				return;
			}
		} else {
			ColumnSnapshot columnSnapshot = columnSnapshotFinder.findById(taskSnapshot.getColumnId());
			
			if (taskMove.getPosition() >= columnSnapshot.getTasks().size()) {
				errors.rejectValue("position", "InvalidPosition");
				return;
			}
		}
		
		
	}
	
}
