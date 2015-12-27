package pl.pszczolkowski.kanban.web.restapi.board;

import static pl.pszczolkowski.kanban.domain.board.entity.Permissions.ADMIN;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardMemberSnapshot;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class UserPermissionsValidator extends AbstractValidator {

	private final BoardSnapshotFinder boardSnapshotFinder;
	
	@Autowired
	public UserPermissionsValidator(BoardSnapshotFinder boardSnapshotFinder) {
		this.boardSnapshotFinder = boardSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserPermissions.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		UserPermissions userPermissions = (UserPermissions) target;
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(userPermissions.getBoardId());
		
		if (boardSnapshot == null || loggedUserIsNotBoardAdmin(boardSnapshot)) {
			errors.rejectValue("boardId", "BoardWithGivenIdDoesntExist");
			return;
		}
		
		if (memberWithGivenIdDoesNotExist(userPermissions.getMemberId(), boardSnapshot)) {
			errors.rejectValue("memberId", "BoardMemberWithGivenIdDoesNotExist");
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
	
	private boolean memberWithGivenIdDoesNotExist(Long memberId, BoardSnapshot boardSnapshot) {
		return boardSnapshot
				.getMembers()
				.stream()
				.noneMatch(m -> m.getId() == memberId);
	}

}
