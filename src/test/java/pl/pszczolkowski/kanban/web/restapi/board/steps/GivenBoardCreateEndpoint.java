package pl.pszczolkowski.kanban.web.restapi.board.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.web.restapi.board.BoardNew;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;

public class GivenBoardCreateEndpoint extends GivenStage<GivenBoardCreateEndpoint> {

	private static final String CLAZZ = GivenBoardCreateEndpoint.class.getSimpleName();
	
	@ProvidedScenarioState
	private BoardNew boardNew;
	
	private void aRequestToEndpoint() throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();

	      String body = objectMapper.writeValueAsString(boardNew);

	      prepareRequest(post("/board")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(body)
	            .accept(MediaType.parseMediaType("application/json;charset=UTF-8")));
	   }
	
	public GivenBoardCreateEndpoint a_request() throws JsonProcessingException {
		boardNew = new BoardNew();
		boardNew.setName(CLAZZ);
		
		aRequestToEndpoint();
		return this;
	}

}
