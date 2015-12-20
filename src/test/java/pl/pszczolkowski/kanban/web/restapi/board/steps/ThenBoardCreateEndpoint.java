package pl.pszczolkowski.kanban.web.restapi.board.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.web.restapi.board.BoardNew;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenBoardCreateEndpoint extends ThenStage<ThenBoardCreateEndpoint> {

	@ExpectedScenarioState
	private BoardNew boardNew;
	@ExpectedScenarioState
	private BoardSnapshotFinder boardSnapshotFinder;
	
 	public ThenBoardCreateEndpoint board_should_be_created() {
		List<BoardSnapshot> boardSnapshots =  boardSnapshotFinder.findByNameAndOWnerId(boardNew.getName(), getLoggedUser().getId());
		BoardSnapshot createdBoardSnapshot = boardSnapshots.get(0);
		
		assertThat(createdBoardSnapshot.getName(), is(equalTo(boardNew.getName())));
		assertThat(createdBoardSnapshot.getOwnerId(), is(equalTo(getLoggedUser().getId())));
		
		return this;
	}

	public ThenBoardCreateEndpoint created_board_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", isA(Integer.class)))
			.andExpect(jsonPath("$.name", is(equalTo(boardNew.getName()))));

		return this;
	}

}
