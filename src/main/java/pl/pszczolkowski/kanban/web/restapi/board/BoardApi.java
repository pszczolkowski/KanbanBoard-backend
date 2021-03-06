package pl.pszczolkowski.kanban.web.restapi.board;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.pszczolkowski.kanban.domain.board.entity.Permissions.ADMIN;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.pszczolkowski.kanban.domain.board.bo.BoardBO;
import pl.pszczolkowski.kanban.domain.board.entity.Permissions;
import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardMemberSnapshot;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.user.finder.UserSnapshotFinder;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;

@RestController
@RequestMapping("/board")
public class BoardApi {

	private final BoardBO boardBO;
	private final BoardSnapshotFinder boardSnapshotFinder;
	private final UserSnapshotFinder userSnapshotFinder;
	private final Validator userInvitationValidator;
	private final Validator userPermissionsValidator;
	
	@Autowired
	public BoardApi(BoardBO boardBO, BoardSnapshotFinder boardSnapshotFinder, UserSnapshotFinder userSnapshotFinder,
			@Qualifier("userInvitationValidator") Validator userInvitationValidator,
			@Qualifier("userPermissionsValidator") Validator userPermissionsValidator) {
		this.boardBO = boardBO;
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.userSnapshotFinder = userSnapshotFinder;
		this.userInvitationValidator = userInvitationValidator;
		this.userPermissionsValidator = userPermissionsValidator;
	}

	@InitBinder("userInvitation")
	protected void initInvitationBinder(WebDataBinder binder) {
		binder.setValidator(userInvitationValidator);
	}
	
	@InitBinder("userPermissions")
	protected void initPermissionsBinder(WebDataBinder binder) {
		binder.setValidator(userPermissionsValidator);
	}
	
	@ApiOperation(
		value = "Get all boards that logged user has access to",
		notes = "Returns all boards that logged user has access to")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Boards returned")})
	@RequestMapping(method = GET)
	public HttpEntity<List<Board>> list() {
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		
		List<BoardSnapshot> boardSnapshots = boardSnapshotFinder.findByMemberId(loggedUserId);
		
		Set<Long> memberIds = new HashSet<>();
		for (BoardSnapshot boardSnapshot : boardSnapshots) {
			memberIds.addAll(
				boardSnapshot
				.getMembers()
				.stream()
				.map(BoardMemberSnapshot::getUserId)
				.collect(toSet()));
		}
		Map<Long, UserSnapshot> userSnapshots = userSnapshotFinder.findAllAsMap(memberIds);
		
		List<Board> boards = boardSnapshotFinder
			.findByMemberId(loggedUserId)
			.stream()
			.map(b -> new Board(b, userSnapshots))
			.collect(toList());
		
		return ResponseEntity
				.ok()
				.body(boards);
	}
	
	private boolean loggedUserIsBoardMember(BoardSnapshot boardSnapshot) {
		return loggedUserIsBoardMember(LoggedUserService.getSnapshot().getId(), boardSnapshot);
	}
	

	private boolean loggedUserIsBoardMember(Long loggedUserId, BoardSnapshot boardSnapshot) {
		return boardSnapshot
				.getMembers()
				.stream()
				.anyMatch(m -> m.getUserId() == loggedUserId);
	}
	
	@ApiOperation(
		value = "Get board by id",
		notes = "Returns board with given id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Board returned"),
		@ApiResponse(code = 404, message = "Board with given id doesn't exist")})
	@RequestMapping("/{boardId}")
	public HttpEntity<Board> get(@PathVariable("boardId") long boardId) {
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(boardId);
		
		if (boardSnapshot == null || !loggedUserIsBoardMember(boardSnapshot)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	
		List<Long> memberIds = boardSnapshot
			.getMembers()
			.stream()
			.map(BoardMemberSnapshot::getUserId)
			.collect(toList());
		
		Map<Long, UserSnapshot> userSnapshots = userSnapshotFinder.findAllAsMap(memberIds);
		
		return ResponseEntity
				.ok()
				.body(new Board(boardSnapshot, userSnapshots));
	}

	@ApiOperation(
		value = "Create new board",
		notes = "Returns created board")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Board created"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@JsonView(View.Summary.class)
	@RequestMapping(
		method = POST, 
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Board> create(@Valid @RequestBody BoardNew boardNew) {
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		BoardSnapshot boardSnapshot = boardBO.create(boardNew.getName(), loggedUserId);
		
		return ResponseEntity
				.status(CREATED)
				.body(new Board(boardSnapshot));
	}
	
	@ApiOperation(
		value = "Invite user as board member",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 200, message = "User invited"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		value = "/inviteUser",
		method = POST, 
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Void> inviteUser(@Valid @RequestBody UserInvitation userInvitation) {
		UserSnapshot userSnapshot = userSnapshotFinder.findByLogin(userInvitation.getLogin());
		boardBO.addMember(userInvitation.getBoardId(), userSnapshot.getId(), Permissions.NORMAL);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(
		value = "Remove board member",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Member removed"),
		@ApiResponse(code = 400, message = "Given input was invalid"),
		@ApiResponse(code = 403, message = "Logged user is not board member")})
	@RequestMapping(
		value = "/{boardId}/member/{userId}",
		method = RequestMethod.DELETE)
	public HttpEntity<Void> removeMember(@PathVariable("boardId") long boardId, @PathVariable("userId") long userId) {
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(boardId);
		if (boardSnapshot == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 
		
		long loggedUserId = LoggedUserService.getSnapshot().getId();
		if (loggedUserId == userId) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else if (!loggedUserIsBoardAdmin(loggedUserId, boardSnapshot)) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		boardBO.removeMember(boardSnapshot.getId(), userId);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private boolean loggedUserIsBoardAdmin(long loggedUserId, BoardSnapshot boardSnapshot) {
		Optional<BoardMemberSnapshot> member = boardSnapshot
				.getMembers()
				.stream()
				.filter(m -> m.getUserId() == loggedUserId)
				.findFirst();
		
		return member.isPresent() && member.get().getPermissions() == ADMIN;
	}
	
	@ApiOperation(
		value = "Set board member permissions",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Permissions set"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		value = "/member/permissions",
		method = POST, 
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Void> setPermissions(@Valid @RequestBody UserPermissions userPermissions) {
		boardBO.setPermissions(userPermissions.getBoardId(), userPermissions.getMemberId(), userPermissions.getPermissions().toDomain());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(
		value = "Leave board",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Board left"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		value = "/{id}/leave",
		method = POST)
	public HttpEntity<Void> leave(@PathVariable("id") long boardId) {
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(boardId);
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		
		if (!loggedUserIsBoardMember(loggedUserId, boardSnapshot)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (boardHasNoOtherAdminThanLoggedUser(loggedUserId, boardSnapshot)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		boardBO.removeMember(boardId, loggedUserId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private boolean boardHasNoOtherAdminThanLoggedUser(Long loggedUserId, BoardSnapshot boardSnapshot) {
		return boardSnapshot
			.getMembers()
			.stream()
			.filter(m -> m.getPermissions() == ADMIN)
			.noneMatch(m -> m.getUserId() != loggedUserId);
	}

	@ApiOperation(
		value = "Delete board",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Board deleted"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		value = "/{id}",
		method = DELETE)
	public HttpEntity<Void> setPermissions(@PathVariable("id") long boardId) {
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(boardId);
		long loggedUserId = LoggedUserService.getSnapshot().getId();
		
		if (!loggedUserIsBoardAdmin(loggedUserId, boardSnapshot)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		boardBO.delete(boardId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
