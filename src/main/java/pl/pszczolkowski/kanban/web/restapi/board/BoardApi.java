package pl.pszczolkowski.kanban.web.restapi.board;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.pszczolkowski.kanban.domain.board.bo.BoardBO;
import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;

@RestController
@RequestMapping("/board")
public class BoardApi {

	private final BoardBO boardBO;
	private final BoardSnapshotFinder boardSnapshotFinder;
	
	@Autowired
	public BoardApi(BoardBO boardBO, BoardSnapshotFinder boardSnapshotFinder) {
		this.boardBO = boardBO;
		this.boardSnapshotFinder = boardSnapshotFinder;
	}

	@ApiOperation(
		value = "Get all boards that logged user has access to",
		notes = "Returns all boards that logged user has access to")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Boards returned")})
	@RequestMapping(method = GET)
	public HttpEntity<List<Board>> list() {
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		
		List<Board> boards = boardSnapshotFinder
			.findByOwnerId(loggedUserId)
			.stream()
			.map(Board::new)
			.collect(toList());
		
		return ResponseEntity
				.ok()
				.body(boards);
	}
	
	@ApiOperation(
		value = "Get board by id",
		notes = "Returns board with given id")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Board returned"),
		@ApiResponse(code = 404, message = "Board with given id doesn't exist")})
	@RequestMapping("/{boardId}")
	public HttpEntity<Board> get(@PathVariable("boardId") long boardId) {
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findByIdAndOwnerId(boardId, loggedUserId);
		
		if (boardSnapshot == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return ResponseEntity
					.ok()
					.body(new Board(boardSnapshot));
		}
	}

	@ApiOperation(
		value = "Create new board",
		notes = "Returns created board")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Board created"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
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

}
