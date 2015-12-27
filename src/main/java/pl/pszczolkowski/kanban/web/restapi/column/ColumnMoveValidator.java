package pl.pszczolkowski.kanban.web.restapi.column;

import static pl.pszczolkowski.kanban.domain.board.entity.Permissions.ADMIN;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardMemberSnapshot;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class ColumnMoveValidator extends AbstractValidator {

	private final BoardSnapshotFinder boardSnapshotFinder;
	private final ColumnSnapshotFinder columnSnapshotFinder;
	
	@Autowired
	public ColumnMoveValidator(BoardSnapshotFinder boardSnapshotFinder, ColumnSnapshotFinder columnSnapshotFinder) {
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.columnSnapshotFinder = columnSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ColumnMove.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		ColumnMove columnMove = (ColumnMove) target;
		
		ColumnSnapshot columnSnapshot = columnSnapshotFinder.findById(columnMove.getColumnId());
		if (columnSnapshot == null) {
			errors.rejectValue("columnId", "ColumnWithGivenIdDoesntExist");
			return;
		}
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(columnSnapshot.getBoardId());
		if (loggedUserIsNotBoardAdmin(boardSnapshot)) {
			errors.rejectValue("columnId", "ColumnWithGivenIdDoesntExist");
			return;
		}
		
		List<ColumnSnapshot> columnSnapshots = columnSnapshotFinder.findByBoardId(columnSnapshot.getBoardId());
		
		if (columnMove.getPosition() >= columnSnapshots.size()) {
			errors.rejectValue("position", "InvalidPosition");
			return;
		}
	}
	
	private boolean loggedUserIsNotBoardAdmin(BoardSnapshot boardSnapshot) {
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		
		Optional<BoardMemberSnapshot> member = boardSnapshot
			.getMembers()
			.stream()
			.filter(m -> m.getUserId() == loggedUserId)
			.findFirst();
		
		return !member.isPresent() || member.get().getPermissions() != ADMIN;
	}
	
}
