package pl.pszczolkowski.kanban.web.restapi.board;

import static pl.pszczolkowski.kanban.domain.board.entity.Permissions.ADMIN;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardMemberSnapshot;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.user.finder.UserSnapshotFinder;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;
import pl.pszczolkowski.kanban.shared.annotations.RestValidator;
import pl.pszczolkowski.kanban.web.restapi.commonvalidation.AbstractValidator;

@RestValidator
public class UserInvitationValidator extends AbstractValidator {

	private final BoardSnapshotFinder boardSnapshotFinder;
	private final UserSnapshotFinder userSnapshotFinder;
	
	@Autowired
	public UserInvitationValidator(BoardSnapshotFinder boardSnapshotFinder, UserSnapshotFinder userSnapshotFinder) {
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.userSnapshotFinder = userSnapshotFinder;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserInvitation.class.isAssignableFrom(clazz);
	}

	@Override
	public void customValidation(Object target, Errors errors) {
		UserInvitation userInvitation = (UserInvitation) target;
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(userInvitation.getBoardId());
		if (boardSnapshot == null || loggedUserIsNotBoardAdmin(boardSnapshot)) {
			errors.rejectValue("boardId", "BoardWithGivenIdDoesntExist");
			return;
		}
		
		UserSnapshot userSnapshot = userSnapshotFinder.findByLogin(userInvitation.getLogin());
		if (userSnapshot == null) {
			errors.rejectValue("login", "UserWithGivenLoginDoesNotExist");
			return;
		}
		
		if (userIsAlreadyBoardMember(userSnapshot, boardSnapshot)) {
			errors.rejectValue("login", "UserIsAlreadyBoardMember");
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

	private boolean userIsAlreadyBoardMember(UserSnapshot userSnapshot, BoardSnapshot boardSnapshot) {
		return boardSnapshot
			.getMembers()
			.stream()
			.anyMatch(m -> m.getUserId() == userSnapshot.getId());
	}
	
}
