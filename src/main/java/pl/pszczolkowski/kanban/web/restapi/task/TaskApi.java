package pl.pszczolkowski.kanban.web.restapi.task;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.bo.TaskBO;
import pl.pszczolkowski.kanban.domain.task.finder.TaskSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;

@RestController
@RequestMapping("/task")
public class TaskApi {
	
	private final TaskBO taskBO;
	private final TaskSnapshotFinder taskSnapshotFinder;
	private final BoardSnapshotFinder boardSnapshotFinder;
	private final Validator taskNewValidator;
	private final Validator taskUpdateValidator;
	private final Validator taskMoveValidator;
	
	@Autowired
	public TaskApi(TaskBO taskBO, TaskSnapshotFinder taskSnapshotFinder, BoardSnapshotFinder boardSnapshotFinder,  
			@Qualifier("taskNewValidator") Validator taskNewValidator,
			@Qualifier("taskUpdateValidator") Validator taskUpdateValidator,
			@Qualifier("taskMoveValidator") Validator taskMoveValidator) {
		this.taskBO = taskBO;
		this.taskSnapshotFinder = taskSnapshotFinder;
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.taskNewValidator = taskNewValidator;
		this.taskUpdateValidator = taskUpdateValidator;
		this.taskMoveValidator = taskMoveValidator;
	}

	@InitBinder("taskNew")
	protected void initNewBinder(WebDataBinder binder) {
		binder.setValidator(taskNewValidator);
	}
	
	@InitBinder("taskUpdate")
	protected void initUpdateBinder(WebDataBinder binder) {
		binder.setValidator(taskUpdateValidator);
	}
	
	@InitBinder("taskMove")
	protected void initMoveBinder(WebDataBinder binder) {
		binder.setValidator(taskMoveValidator);
	}
	
	@ApiOperation(
		value = "Get task by id",
		notes = "Returns task with given id")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Task with given id returned"),
		@ApiResponse(code = 404, message = "Task with given id does not exist")})
	@RequestMapping(
		value = "/{id}",
		method = GET)
	public HttpEntity<Task> get(@PathVariable("id") long taskId) {
		TaskSnapshot taskSnapshot = taskSnapshotFinder.findById(taskId);
		if (taskSnapshot == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(taskSnapshot.getBoardId());
		if (boardSnapshot.getOwnerId() != LoggedUserService.getSnapshot().getId()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity
				.ok()
				.body(new Task(taskSnapshot));
	}
	
	@ApiOperation(
		value = "Create new task",
		notes = "Returns created task")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Task created"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		method = POST,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Task> create(@Valid @RequestBody TaskNew taskNew) {
		TaskSnapshot taskSnapshot = taskBO.create(taskNew.getColumnId(), taskNew.getTitle(), taskNew.getDescription(),
				taskNew.getAssigneeId(), taskNew.getLabelId());
		return ResponseEntity
				.status(CREATED)
				.body(new Task(taskSnapshot));
	}
	
	@ApiOperation(
		value = "Update task",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Task updated"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		method = PUT,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Task> update(@Valid @RequestBody TaskUpdate taskUpdate) {
		taskBO.edit(taskUpdate.getId(), taskUpdate.getTitle(), taskUpdate.getDescription());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(
		value = "Move task",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Task moved"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		value = "/move",
		method = POST,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Void> move(@Valid @RequestBody TaskMove taskMove) {
		taskBO.move(taskMove.getTaskId(), taskMove.getColumnId(), taskMove.getPosition());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
