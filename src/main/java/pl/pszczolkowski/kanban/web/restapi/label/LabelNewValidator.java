package pl.pszczolkowski.kanban.web.restapi.label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.entity.Permissions;
import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.label.finder.LabelSnapshotFinder;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class LabelNewValidator extends AbstractValidator {

	private final BoardSnapshotFinder boardSnapshotFinder;
	private final LabelSnapshotFinder labelSnapshotFinder;

	@Autowired
	public LabelNewValidator(BoardSnapshotFinder boardSnapshotFinder, LabelSnapshotFinder labelSnapshotFinder) {
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.labelSnapshotFinder = labelSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return LabelNew.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		LabelNew labelNew = (LabelNew) target;
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(labelNew.getBoardId());
		
		if (boardSnapshot == null) {
			errors.rejectValue("boardId", "BoardWithGivenIdDoesntExist");
			return;
		}
		
		if (loggedUserIsNotBoardAdmin(boardSnapshot)) {
			errors.rejectValue("boardId", "BoardWithGivenIdDoesntExist");
			return;
		}
		
		if (labelSnapshotFinder.findByBoardIdAndName(labelNew.getBoardId(), labelNew.getName()) != null) {
			errors.rejectValue("name", "LabelWithGivenNameAlreadyExistsOnTheBoard");
			return;
		}
	}

	private boolean loggedUserIsNotBoardAdmin(BoardSnapshot boardSnapshot) {
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		
		return boardSnapshot
			.getMembers()
			.stream()
			.filter(m -> m.getUserId() == loggedUserId)
			.filter(m -> m.getPermissions() == Permissions.ADMIN)
			.findFirst()
			.map(m -> false)
			.orElse(true);
	}
	
}
