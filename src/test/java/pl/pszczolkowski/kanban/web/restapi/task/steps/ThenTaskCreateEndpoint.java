package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenTaskCreateEndpoint extends ThenStage<ThenTaskCreateEndpoint> {

	public ThenTaskCreateEndpoint created_task_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", isA(Integer.class)))
			.andExpect(jsonPath("$.idOnBoard", isA(Integer.class)))
			.andExpect(jsonPath("$.boardId", isA(Integer.class)))
			.andExpect(jsonPath("$.title", isA(String.class)))
			.andExpect(jsonPath("$.position", isA(Integer.class)))
			.andExpect(jsonPath("$.priority", isA(String.class)))
			.andExpect(jsonPath("$.size", isA(Double.class)))
			.andExpect(jsonPath("$.createdAt").exists())
			.andExpect(jsonPath("$.columnId", isA(Integer.class)));
	
		return this;
	}
	
}
