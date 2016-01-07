package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenTaskGetEndpoint extends ThenStage<ThenTaskGetEndpoint> {

	@ExpectedScenarioState
	private TaskSnapshot taskSnapshot;
	
	public ThenTaskGetEndpoint requested_task_should_be_returned() throws Exception {
		getResult()
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", is(equalTo((int) taskSnapshot.getId()))))
			.andExpect(jsonPath("$.columnId", is(equalTo((int) taskSnapshot.getColumnId()))))
			.andExpect(jsonPath("$.idOnBoard", is(equalTo(taskSnapshot.getIdOnBoard()))))
			.andExpect(jsonPath("$.boardId", is(equalTo((int) taskSnapshot.getBoardId()))))
			.andExpect(jsonPath("$.title", is(equalTo(taskSnapshot.getTitle()))))
			.andExpect(jsonPath("$.description", is(equalTo(taskSnapshot.getDescription()))))
			.andExpect(jsonPath("$.position", is(equalTo(taskSnapshot.getPosition()))))
			.andExpect(jsonPath("$.priority", is(equalTo(taskSnapshot.getPriority().toString()))))
			.andExpect(jsonPath("$.size", is(equalTo((double) taskSnapshot.getSize()))));
	
		return this;
	}

}
