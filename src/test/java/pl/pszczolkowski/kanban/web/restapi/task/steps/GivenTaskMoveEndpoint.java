package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;
import static pl.pszczolkowski.kanban.builder.ColumnBuilder.aColumn;
import static pl.pszczolkowski.kanban.builder.TaskBuilder.aTask;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;
import pl.pszczolkowski.kanban.web.restapi.task.TaskMove;

public class GivenTaskMoveEndpoint extends GivenStage<GivenTaskMoveEndpoint> {
	
	@ProvidedScenarioState
	private final List<TaskSnapshot> taskSnapshots = new ArrayList<>();
	
	private ColumnSnapshot columnSnapshot;
	private BoardSnapshot boardSnapshot;

	public GivenTaskMoveEndpoint a_board() {
		boardSnapshot = aBoard()
			.withAuthorId(getLoggedUser().getId())
			.build();
		
		return this;
	}

	public GivenTaskMoveEndpoint a_board_that_logged_user_has_no_access_to() {
		boardSnapshot = aBoard().build();
		return this;
	}

	public GivenTaskMoveEndpoint a_column() {
		columnSnapshot = aColumn()
			.withBoardId(boardSnapshot.getId())
			.build();
		
		return this;
	}

	public GivenTaskMoveEndpoint a_request() throws JsonProcessingException {
		TaskMove taskMove = new TaskMove();
		taskMove.setTaskId(taskSnapshots.get(0).getId());
		taskMove.setPosition(taskSnapshots.size() - 1);
		taskMove.setColumnId(columnSnapshot.getId());
		
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(taskMove);

		prepareRequest(
			post("/task/move")
			.contentType(MediaType.APPLICATION_JSON)
			.content(body)
			.accept(MediaType.APPLICATION_JSON_VALUE));
		
		return this;
	}

	public GivenTaskMoveEndpoint some_tasks() {
		for (int i = 0; i < 5; i++) {
			taskSnapshots.add(
				aTask()
					.withColumn(columnSnapshot)
					.build());
		}
		
		return this;
	}

}
