package pl.pszczolkowski.kanban.web.restapi.label;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static pl.pszczolkowski.kanban.domain.board.entity.Permissions.ADMIN;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.label.bo.LabelBO;
import pl.pszczolkowski.kanban.domain.label.finder.LabelSnapshotFinder;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;

@RestController
@RequestMapping("/label")
public class LabelApi {
	
	private final LabelBO labelBO;
	private final LabelSnapshotFinder labelSnapshotFinder;
	private final BoardSnapshotFinder boardSnapshotFinder;
	private final Validator labelNewValidator;
	
	@Autowired
	public LabelApi(LabelBO labelBO, LabelSnapshotFinder labelSnapshotFinder, BoardSnapshotFinder boardSnapshotFinder, 
			@Qualifier("labelNewValidator") Validator labelNewValidator) {
		this.labelBO = labelBO;
		this.labelSnapshotFinder = labelSnapshotFinder;
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.labelNewValidator = labelNewValidator;
	}

	@InitBinder("labelNew")
	protected void initNewBinder(WebDataBinder binder) {
		binder.setValidator(labelNewValidator);
	}
	
	@ApiOperation(
		value = "Get all labels of the board",
		notes = "Returns all labels of board")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Labels returned"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		params = {"boardId"},
		method = GET)
	public HttpEntity<List<Label>> list(@RequestParam("boardId") long boardId) {
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(boardId);
		if (boardSnapshot == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
			
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		if (!userIsBoardMember(loggedUserId, boardSnapshot)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<Label> labels = labelSnapshotFinder
			.findByBoardId(boardId)
			.stream()
			.map(Label::new)
			.collect(toList());
		
		return ResponseEntity
				.ok()
				.body(labels);
	}
	
	private boolean userIsBoardMember(Long loggedUserId, BoardSnapshot boardSnapshot) {
		return boardSnapshot
			.getMembers()
			.stream()
			.anyMatch(m -> m.getUserId() == loggedUserId);
	}

	@ApiOperation(
		value = "Create new label",
		notes = "Returns created label")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Label created"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		method = POST,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Label> create(@Valid @RequestBody LabelNew labelNew) {
		LabelSnapshot labelSnapshot = labelBO.create(labelNew.getBoardId(), labelNew.getName(), labelNew.getColor());
		return ResponseEntity
				.status(CREATED)
				.body(new Label(labelSnapshot));
	}
	
	@ApiOperation(
		value = "Delete label",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Label deleted"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		value = "/{id}",
		method = DELETE)
	public HttpEntity<Void> delete(@PathVariable("id") long labelId) {
		LabelSnapshot labelSnapshot = labelSnapshotFinder.findById(labelId);
		if (labelSnapshot != null) {
			BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(labelSnapshot.getBoardId());
			if (!loggedUserIsBoardAdmin(boardSnapshot)) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			labelBO.delete(labelId);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private boolean loggedUserIsBoardAdmin(BoardSnapshot boardSnapshot) {
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		
		return boardSnapshot
			.getMembers()
			.stream()
			.filter(m -> m.getId() == loggedUserId)
			.filter(m -> m.getPermissions() == ADMIN)
			.findAny()
			.isPresent();
	}
	
}
