package pl.pszczolkowski.kanban.web.restapi.board.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenBoardListEndpoint extends ThenStage<ThenBoardListEndpoint> {

	@ExpectedScenarioState
	private List<BoardSnapshot> boardSnapshots;
	
	@SuppressWarnings("unchecked")
	public ThenBoardListEndpoint list_of_boards_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$", hasSize(equalTo(boardSnapshots.size()))))
			.andExpect(jsonPath("$[*].id", hasItems(isA(Integer.class))))
			.andExpect(jsonPath("$[*].name", hasItems(isA(String.class))));
	
		return this;
	}

}
