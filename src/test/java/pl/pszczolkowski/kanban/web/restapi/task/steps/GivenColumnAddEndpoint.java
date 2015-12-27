package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;
import static pl.pszczolkowski.kanban.builder.ColumnBuilder.aColumn;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.web.restapi.column.ColumnNew;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;

public class GivenColumnAddEndpoint extends GivenStage<GivenColumnAddEndpoint> {

	private static final String CLAZZ = GivenColumnAddEndpoint.class.getSimpleName().substring(0, 20);
	@ProvidedScenarioState
	private BoardSnapshot boardSnapshot;
	@ProvidedScenarioState
	private ColumnNew columnNew;
	
	private ColumnSnapshot columnSnapshot;
	
	public GivenColumnAddEndpoint a_board_that_logged_user_has_access_to() {
		boardSnapshot = aBoard()
			.withAuthorId(getLoggedUser().getId())
			.build();
		
		return this;
	}
	
	private void aRequestToEndpoint() throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();

	      String body = objectMapper.writeValueAsString(columnNew);

	      prepareRequest(post("/column")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(body)
	            .accept(MediaType.parseMediaType("application/json;charset=UTF-8")));
	   }
	
	public GivenColumnAddEndpoint a_request() throws JsonProcessingException {
		columnNew = new ColumnNew();
		columnNew.setBoardId(boardSnapshot.getId());
		columnNew.setName(CLAZZ);
		columnNew.setWorkInProgressLimit(1);
		
		aRequestToEndpoint();
		return this;
	}

	public GivenColumnAddEndpoint a_request_with_not_existing_board_id() throws JsonProcessingException {
		long notExistingBoardId = Long.MAX_VALUE;
		
		columnNew = new ColumnNew();
		columnNew.setBoardId(notExistingBoardId);
		columnNew.setName(CLAZZ);
		columnNew.setWorkInProgressLimit(1);
		
		aRequestToEndpoint();
		return this;
	}

	public GivenColumnAddEndpoint a_board_that_logged_user_has_no_access_to() {
		boardSnapshot = aBoard().build();
		return this;
	}

	public GivenColumnAddEndpoint a_column() {
		columnSnapshot = aColumn()
			.withName(CLAZZ)
			.withBoardId(boardSnapshot.getId())
			.build();
		
		return this;
	}

	public GivenColumnAddEndpoint a_request_to_add_column_with_the_same_name() throws JsonProcessingException {
		columnNew = new ColumnNew();
		columnNew.setBoardId(boardSnapshot.getId());
		columnNew.setName(columnSnapshot.getName());
		columnNew.setWorkInProgressLimit(1);
		
		aRequestToEndpoint();
		return this;
	}

}
