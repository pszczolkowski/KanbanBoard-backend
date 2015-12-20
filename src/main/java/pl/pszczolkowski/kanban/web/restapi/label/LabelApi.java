package pl.pszczolkowski.kanban.web.restapi.label;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
		if (boardSnapshot.getOwnerId() != loggedUserId) {
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
	
}
