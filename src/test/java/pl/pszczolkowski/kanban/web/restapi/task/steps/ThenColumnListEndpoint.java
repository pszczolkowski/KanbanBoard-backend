package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenColumnListEndpoint extends ThenStage<ThenColumnListEndpoint> {

	@ExpectedScenarioState
	private List<ColumnSnapshot> columnSnapshots;
	
	@SuppressWarnings("unchecked")
	public ThenColumnListEndpoint list_of_columns_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$", hasSize(equalTo(columnSnapshots.size()))))
			.andExpect(jsonPath("$[*].id", hasItems(isA(Integer.class))))
			.andExpect(jsonPath("$[*].name", hasItems(isA(String.class))))
			.andExpect(jsonPath("$[*].boardId", hasItems(isA(Integer.class))))
			.andExpect(jsonPath("$[*].position", hasItems(isA(Integer.class))))
			.andExpect(jsonPath("$[*].workInProgressLimit", hasItems(isA(Integer.class))));
	
		return this;
	}


}
