package pl.pszczolkowski.kanban.web.restapi.column.steps;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.web.restapi.column.ColumnNew;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenColumnAddEndpoint extends ThenStage<ThenColumnAddEndpoint> {

	@ExpectedScenarioState
	private ColumnSnapshotFinder columnSnapshotFinder;
	@ExpectedScenarioState
	private BoardSnapshot boardSnapshot;
	@ExpectedScenarioState
	private ColumnNew columnNew;
	
	public ThenColumnAddEndpoint column_should_be_created() {
		ColumnSnapshot columnSnapshot = columnSnapshotFinder.findByNameAndBoardId(columnNew.getName(), boardSnapshot.getId());
		
		assertThat(columnSnapshot, is(notNullValue()));
		assertThat(columnSnapshot.getWorkInProgressLimit(), is(equalTo(columnNew.getWorkInProgressLimit())));
		
		return this;
	}

	public ThenColumnAddEndpoint created_board_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", isA(Integer.class)))
			.andExpect(jsonPath("$.name", is(equalTo(columnNew.getName()))));

		return this;
	}

	public ThenColumnAddEndpoint created_column_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", isA(Integer.class)))
			.andExpect(jsonPath("$.name", is(equalTo(columnNew.getName()))))
			.andExpect(jsonPath("$.workInProgressLimit", is(equalTo(columnNew.getWorkInProgressLimit()))));
	
		return this;
	}

}
