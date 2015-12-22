package pl.pszczolkowski.kanban.web.restapi.task;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.pszczolkowski.kanban.domain.task.bo.TaskBO;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;

@RestController
@RequestMapping("/task")
public class TaskApi {
	
	private final TaskBO taskBO;
	private final Validator taskNewValidator;
	private final Validator taskMoveValidator;
	
	@Autowired
	public TaskApi(TaskBO taskBO, @Qualifier("taskNewValidator") Validator taskNewValidator,
			@Qualifier("taskMoveValidator") Validator taskMoveValidator) {
		this.taskBO = taskBO;
		this.taskNewValidator = taskNewValidator;
		this.taskMoveValidator = taskMoveValidator;
	}

	@InitBinder("taskNew")
	protected void initNewBinder(WebDataBinder binder) {
		binder.setValidator(taskNewValidator);
	}
	
	@InitBinder("taskMove")
	protected void initMoveBinder(WebDataBinder binder) {
		binder.setValidator(taskMoveValidator);
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
