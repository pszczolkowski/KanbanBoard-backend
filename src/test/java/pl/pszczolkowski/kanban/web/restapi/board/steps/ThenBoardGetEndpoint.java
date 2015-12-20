package pl.pszczolkowski.kanban.web.restapi.board.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenBoardGetEndpoint extends ThenStage<ThenBoardGetEndpoint> {

	@ExpectedScenarioState
	private BoardSnapshot boardSnapshot;

	public ThenBoardGetEndpoint requested_board_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", is(equalTo((int)boardSnapshot.getId()))))
			.andExpect(jsonPath("$.name", is(equalTo(boardSnapshot.getName()))));
	
		return this;
	}

}
