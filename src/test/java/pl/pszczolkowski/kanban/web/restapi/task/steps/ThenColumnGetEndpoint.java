package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenColumnGetEndpoint extends ThenStage<ThenColumnGetEndpoint> {

	@ExpectedScenarioState
	private ColumnSnapshot columnSnapshot;
	
	public ThenColumnGetEndpoint requested_column_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", is(equalTo((int) columnSnapshot.getId()))))
			.andExpect(jsonPath("$.name", is(equalTo(columnSnapshot.getName()))))
			.andExpect(jsonPath("$.boardId", is(equalTo((int) columnSnapshot.getBoardId()))))
			.andExpect(jsonPath("$.position", is(equalTo(columnSnapshot.getPosition()))))
			.andExpect(jsonPath("$.workInProgressLimit", is(equalTo(columnSnapshot.getWorkInProgressLimit()))));
	
		return this;
	}

}
