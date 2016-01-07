package pl.pszczolkowski.kanban.web.restapi.label.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.web.restapi.label.LabelNew;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;

public class GivenLabelCreateEndpoint extends GivenStage<GivenLabelCreateEndpoint> {

	private static final String CLAZZ = GivenLabelCreateEndpoint.class.getSimpleName().substring(0, 20);
	
	@ProvidedScenarioState
	private LabelNew labelNew;
	
	private BoardSnapshot boardSnapshot;

	
	public GivenLabelCreateEndpoint a_board_that_logged_user_has_access_to() {
		boardSnapshot = aBoard()
			.withAuthorId(getLoggedUser().getId())
			.build();
		
		return this;
	}
	
	private void aRequestToEndpoint() throws JsonProcessingException {
	      ObjectMapper objectMapper = new ObjectMapper();
	      String body = objectMapper.writeValueAsString(labelNew);

	      prepareRequest(post("/label")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(body)
	            .accept(MediaType.parseMediaType("application/json;charset=UTF-8")));
	   }
	
	public GivenLabelCreateEndpoint a_request() throws JsonProcessingException {
		labelNew = new LabelNew();
		labelNew.setBoardId(boardSnapshot.getId());
		labelNew.setName(CLAZZ);
		labelNew.setColor("#ffffff");
		
		aRequestToEndpoint();
		return this;
	}

	public GivenLabelCreateEndpoint a_board_that_logged_user_has_no_access_to() {
		boardSnapshot = aBoard().build();
		return this;
	}

}
