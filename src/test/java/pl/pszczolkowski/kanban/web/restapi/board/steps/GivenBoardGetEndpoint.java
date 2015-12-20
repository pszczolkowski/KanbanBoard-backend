package pl.pszczolkowski.kanban.web.restapi.board.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;

public class GivenBoardGetEndpoint extends GivenStage<GivenBoardGetEndpoint> {

	@ProvidedScenarioState
	private BoardSnapshot boardSnapshot;
	
	public GivenBoardGetEndpoint a_board_for_logged_user() {
		boardSnapshot = aBoard()
			.withOwnerId(getLoggedUser().getId())
			.build();
		
		return this;
	}
	
	public GivenBoardGetEndpoint a_request() throws JsonProcessingException {
		prepareRequest(get("/board/{boardId}", boardSnapshot.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.parseMediaType("application/json;charset=UTF-8")));
		
		return this;
	}

	public GivenBoardGetEndpoint a_request_to_get_not_existing_board() {
		int notExistingBoardId = Integer.MAX_VALUE;
		
		prepareRequest(get("/board/{boardId}", notExistingBoardId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.parseMediaType("application/json;charset=UTF-8")));
		
		return this;
	}

	public GivenBoardGetEndpoint a_board_that_logged_user_has_no_access_to() {
		boardSnapshot = aBoard()
			.build();
		
		return this;
	}

}
