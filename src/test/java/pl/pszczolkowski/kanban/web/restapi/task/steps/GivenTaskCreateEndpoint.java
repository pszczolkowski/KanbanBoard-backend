package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;
import static pl.pszczolkowski.kanban.builder.ColumnBuilder.aColumn;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;
import pl.pszczolkowski.kanban.web.restapi.task.TaskNew;

public class GivenTaskCreateEndpoint extends GivenStage<GivenTaskCreateEndpoint> {

	private static final String CLAZZ = GivenTaskCreateEndpoint.class.getSimpleName().substring(0, 20);
	
	private TaskNew taskNew;
	private BoardSnapshot boardSnapshot;
	private ColumnSnapshot columnSnapshot;
	
	public GivenTaskCreateEndpoint a_board_that_logged_user_has_access_to() {
		boardSnapshot = aBoard()
			.withAuthorId(getLoggedUser().getId())
			.build();
		
		return this;
	}
	
	private void aRequestToEndpoint() throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();

	      String body = objectMapper.writeValueAsString(taskNew);

	      prepareRequest(post("/task")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(body)
	            .accept(MediaType.parseMediaType("application/json;charset=UTF-8")));
	   }
	
	public GivenTaskCreateEndpoint a_request() throws JsonProcessingException {
		taskNew = new TaskNew();
		taskNew.setColumnId(columnSnapshot.getId());
		taskNew.setTitle(CLAZZ);
		taskNew.setSize(1);
		
		aRequestToEndpoint();
		return this;
	}

	public GivenTaskCreateEndpoint a_request_with_not_existing_board_id() throws JsonProcessingException {
		taskNew = new TaskNew();
		taskNew.setColumnId(columnSnapshot.getId());
		taskNew.setTitle(CLAZZ);
		taskNew.setSize(1);
		
		aRequestToEndpoint();
		return this;
	}

	public GivenTaskCreateEndpoint a_board_that_logged_user_has_no_access_to() {
		boardSnapshot = aBoard().build();
		return this;
	}

	public GivenTaskCreateEndpoint a_column() {
		columnSnapshot = aColumn()
			.withName(CLAZZ)
			.withBoardId(boardSnapshot.getId())
			.build();
		
		return this;
	}

}
