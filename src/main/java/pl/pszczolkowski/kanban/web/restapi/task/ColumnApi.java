package pl.pszczolkowski.kanban.web.restapi.task;

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
import pl.pszczolkowski.kanban.domain.task.bo.ColumnBO;
import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;

@RestController
@RequestMapping("/column")
public class ColumnApi {

	private final ColumnBO columnBO;
	private final ColumnSnapshotFinder columnSnapshotFinder;
	private final BoardSnapshotFinder boardSnapshotFinder;
	private final Validator columnNewValidator;

	@Autowired
	public ColumnApi(ColumnBO columnBO, ColumnSnapshotFinder columnSnapshotFinder, BoardSnapshotFinder boardSnapshotFinder, Validator columnNewValidator) {
		this.columnBO = columnBO;
		this.columnSnapshotFinder = columnSnapshotFinder;
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.columnNewValidator = columnNewValidator;
	}
	
	@InitBinder("columnNew")
	protected void initNewBinder(WebDataBinder binder) {
		binder.setValidator(columnNewValidator);
	}
	
	@ApiOperation(
		value = "Get all columns of the board",
		notes = "Returns all columns of board")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Columns returned"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		params = {"boardId"},
		method = GET)
	public HttpEntity<List<Column>> list(@RequestParam("boardId") long boardId) {
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(boardId);
		if (boardSnapshot == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
			
		Long loggedUserId = LoggedUserService.getSnapshot().getId();
		if (boardSnapshot.getOwnerId() != loggedUserId) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<Column> columns = columnSnapshotFinder
			.findByBoardId(boardId)
			.stream()
			.map(Column::new)
			.collect(toList());
		
		return ResponseEntity
				.ok()
				.body(columns);
	}
	
	@ApiOperation(
		value = "Get column by id",
		notes = "Returns column with given id")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Column returned"),
		@ApiResponse(code = 404, message = "Column with given id doesn't exist")})
	@RequestMapping(
		value = "/{columnId}",
		method = GET)
	public HttpEntity<Column> get(@PathVariable("columnId") long columnId) {
		ColumnSnapshot columnSnapshot = columnSnapshotFinder.findById(columnId);
		if (columnSnapshot == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(columnSnapshot.getBoardId());
		if (boardSnapshot.getOwnerId() != LoggedUserService.getSnapshot().getId()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity
				.ok()
				.body(new Column(columnSnapshot));
	}
	
	@ApiOperation(
		value = "Add new column to board",
		notes = "Returns added column")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Column added"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		method = POST, 
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Column> add(@Valid @RequestBody ColumnNew columnNew) {
		ColumnSnapshot columnSnapshot = columnBO.add(columnNew.getBoardId(), columnNew.getName(), columnNew.getWorkInProgressLimit());
		return ResponseEntity
				.status(CREATED)
				.body(new Column(columnSnapshot));
	}
	
}
