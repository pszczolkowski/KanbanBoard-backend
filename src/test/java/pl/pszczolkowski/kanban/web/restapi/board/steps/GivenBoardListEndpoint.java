package pl.pszczolkowski.kanban.web.restapi.board.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;

public class GivenBoardListEndpoint extends GivenStage<GivenBoardListEndpoint> {

	@ProvidedScenarioState
	private final List<BoardSnapshot> boardSnapshots = new ArrayList<>();
	
	public GivenBoardListEndpoint some_boards() {
		Long loggedUserId = getLoggedUser().getId();
		
		boardSnapshots.add(
			aBoard()
				.withAuthorId(loggedUserId)
				.build());
		boardSnapshots.add(
			aBoard()
				.withAuthorId(loggedUserId)
				.build());
		
		aBoard().build();
		
		return this;
	}
	
	public GivenBoardListEndpoint a_request() throws JsonProcessingException {
		prepareRequest(get("/board")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.parseMediaType("application/json;charset=UTF-8")));
		
		return this;
	}

}
