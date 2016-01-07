package pl.pszczolkowski.kanban.web.restapi.label.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenLabelListEndpoint extends ThenStage<ThenLabelListEndpoint> {

	@ExpectedScenarioState
	private List<LabelSnapshot> labelSnapshots;
	
	@SuppressWarnings("unchecked")
	public ThenLabelListEndpoint list_of_labels_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$", hasSize(equalTo(labelSnapshots.size()))))
			.andExpect(jsonPath("$[*].id", hasItems(isA(Integer.class))))
			.andExpect(jsonPath("$[*].boardId", hasItems(isA(Integer.class))))
			.andExpect(jsonPath("$[*].name", hasItems(isA(String.class))))
			.andExpect(jsonPath("$[*].color", hasItems(isA(String.class))));

		return this;
	}

}
