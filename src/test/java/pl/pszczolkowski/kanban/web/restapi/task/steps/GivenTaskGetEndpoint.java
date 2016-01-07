package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;
import static pl.pszczolkowski.kanban.builder.ColumnBuilder.aColumn;
import static pl.pszczolkowski.kanban.builder.TaskBuilder.aTask;

import org.springframework.http.MediaType;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;

public class GivenTaskGetEndpoint extends GivenStage<GivenTaskGetEndpoint> {
	
	@ProvidedScenarioState
	private TaskSnapshot taskSnapshot;
	
	private ColumnSnapshot columnSnapshot;
	private BoardSnapshot boardSnapshot;

	public GivenTaskGetEndpoint a_board() {
		boardSnapshot = aBoard()
			.withAuthorId(getLoggedUser().getId())
			.build();
		
		return this;
	}

	public GivenTaskGetEndpoint a_board_that_logged_user_has_no_access_to() {
		boardSnapshot = aBoard().build();
		return this;
	}

	public GivenTaskGetEndpoint a_column() {
		columnSnapshot = aColumn()
			.withBoardId(boardSnapshot.getId())
			.build();
		
		return this;
	}

	public GivenTaskGetEndpoint a_request() {
		prepareRequest(
			get("/task/{id}", taskSnapshot.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_VALUE));
		
		return this;
	}

	public GivenTaskGetEndpoint a_task() {
		taskSnapshot = aTask()
			.withColumn(columnSnapshot)
			.build();
		
		return this;
	}

	public GivenTaskGetEndpoint a_request_with_not_existing_task_id() {
		long notExistingTaskId = Long.MAX_VALUE;
		
		prepareRequest(
			get("/task/{id}", notExistingTaskId)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_VALUE));
		
		return this;
	}

}
