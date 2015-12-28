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
	private final Validator labelAssignValidator;
	private final Validator userAssignValidator;
	
	@Autowired
	public TaskApi(TaskBO taskBO, TaskSnapshotFinder taskSnapshotFinder, BoardSnapshotFinder boardSnapshotFinder,  
			@Qualifier("taskNewValidator") Validator taskNewValidator,
			@Qualifier("taskUpdateValidator") Validator taskUpdateValidator,
			@Qualifier("taskMoveValidator") Validator taskMoveValidator,
			@Qualifier("labelAssignValidator") Validator labelAssignValidator,
			@Qualifier("userAssignValidator") Validator userAssignValidator) {
		this.taskBO = taskBO;
		this.taskSnapshotFinder = taskSnapshotFinder;
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.taskNewValidator = taskNewValidator;
		this.taskUpdateValidator = taskUpdateValidator;
		this.taskMoveValidator = taskMoveValidator;
		this.labelAssignValidator = labelAssignValidator;
		this.userAssignValidator = userAssignValidator;
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
	
	@InitBinder("labelAssign")
	protected void initLabelAssignBinder(WebDataBinder binder) {
		binder.setValidator(labelAssignValidator);
	}
	
	@InitBinder("userAssign")
	protected void initUserAssignBinder(WebDataBinder binder) {
		binder.setValidator(userAssignValidator);
	}
	
	private boolean userIsBoardMember(long loggedUserId, BoardSnapshot boardSnapshot) {
		return boardSnapshot
			.getMembers()
			.stream()
			.anyMatch(m -> m.getUserId() == loggedUserId);
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
		long loggedUserId = LoggedUserService.getSnapshot().getId();
		if (!userIsBoardMember(loggedUserId , boardSnapshot)) {
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
		@ApiResponse(code = 200, message = "Task updated"),
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
		@ApiResponse(code = 200, message = "Task moved"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		value = "/move",
		method = POST,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Void> move(@Valid @RequestBody TaskMove taskMove) {
		taskBO.move(taskMove.getTaskId(), taskMove.getColumnId(), taskMove.getPosition());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(
		value = "Assign label to task",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Label assigned"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		value = "/label",
		method = POST,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Void> assignLabel(@Valid @RequestBody LabelAssign labelAssign) {
		taskBO.assignLabel(labelAssign.getTaskId(), labelAssign.getLabelId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@ApiOperation(
		value = "Assign user to task",
		notes = "Returns empty body")
	@ApiResponses({
		@ApiResponse(code = 200, message = "User assigned"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(
		value = "/assignUser",
		method = POST,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Void> assignUser(@Valid @RequestBody UserAssign userAssign) {
		taskBO.assignUser(userAssign.getTaskId(), userAssign.getAssigneeId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
