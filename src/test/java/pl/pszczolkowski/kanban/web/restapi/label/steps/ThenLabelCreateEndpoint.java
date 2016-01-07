package pl.pszczolkowski.kanban.web.restapi.label.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.web.restapi.label.LabelNew;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenLabelCreateEndpoint extends ThenStage<ThenLabelCreateEndpoint> {

	@ExpectedScenarioState
	private LabelNew labelNew;
	
	public ThenLabelCreateEndpoint created_label_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", isA(Integer.class)))
			.andExpect(jsonPath("$.boardId", is(equalTo((int) labelNew.getBoardId()))))
			.andExpect(jsonPath("$.name", is(equalTo(labelNew.getName()))))
			.andExpect(jsonPath("$.color", is(equalTo(labelNew.getColor()))));
			
		return this;
	}

	
}
