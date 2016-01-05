package pl.pszczolkowski.kanban.web.restapi.label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.entity.Permissions;
import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.label.finder.LabelSnapshotFinder;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class LabelUpdateValidator extends AbstractValidator {

	private final BoardSnapshotFinder boardSnapshotFinder;
	private final LabelSnapshotFinder labelSnapshotFinder;

	@Autowired
	public LabelUpdateValidator(BoardSnapshotFinder boardSnapshotFinder, LabelSnapshotFinder labelSnapshotFinder) {
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.labelSnapshotFinder = labelSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return LabelUpdate.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		LabelUpdate labelUpdate = (LabelUpdate) target;
		LabelSnapshot labelSnapshot = labelSnapshotFinder.findById(labelUpdate.getId());
		if (labelSnapshot == null) {
			errors.rejectValue("id", "LabelWithGivenIdDoesNotExist");
			return;
		}
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(labelSnapshot.getBoardId());
		if (boardSnapshot == null || loggedUserIsNotBoardAdmin(boardSnapshot)) {
			errors.rejectValue("id", "LabelWithGivenIdDoesNotExist");
			return;
		}
		
		LabelSnapshot labelWithGivenNameSnapshot = labelSnapshotFinder.findByBoardIdAndName(labelSnapshot.getBoardId(), labelUpdate.getName());
		if (labelWithGivenNameSnapshot != null && labelWithGivenNameSnapshot.getId() != labelSnapshot.getId()) {
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
