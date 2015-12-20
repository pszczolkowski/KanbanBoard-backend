package pl.pszczolkowski.kanban.web.restapi.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class ColumnNewValidator extends AbstractValidator {

	private final BoardSnapshotFinder boardSnapshotFinder;
	private final ColumnSnapshotFinder columnSnapshotFinder;
	
	@Autowired
	public ColumnNewValidator(BoardSnapshotFinder boardSnapshotFinder, ColumnSnapshotFinder columnSnapshotFinder) {
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.columnSnapshotFinder = columnSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ColumnNew.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		ColumnNew columnNew = (ColumnNew) target;
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findByIdAndOwnerId(columnNew.getBoardId(), loggedUserId);
		if (boardSnapshot == null) {
			errors.rejectValue("boardId", "BoardWithGivenIdDoesntExist");
			return;
		}
		
		if (boardSnapshot.getOwnerId() != loggedUserId) {
			// user has no rights to access given board
			errors.rejectValue("boardId", "BoardWithGivenIdDoesntExist");
		}
		
		ColumnSnapshot columnSnapshot = columnSnapshotFinder.findByNameAndBoardId(columnNew.getName(), columnNew.getBoardId());
		
		if (columnSnapshot != null) {
			errors.rejectValue("name", "ColumnWithGivenNameAlreadyExistsInTheBoard");
		}
	}
	
}