package pl.pszczolkowski.kanban.web.restapi.shared;

import static com.tngtech.jgiven.annotation.ScenarioState.Resolution.NAME;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

public class ThenStage<SELF extends ThenStage<?>> extends Stage<SELF> {

	@ExpectedScenarioState
	private ResultActions result;
	@ExpectedScenarioState(resolution = NAME)
	private UserSnapshot loggedUserSnapshot;
	
	protected UserSnapshot getLoggedUser() {
		return loggedUserSnapshot;
	}
	
	protected ResultActions getResult() {
		return result;
	}
	
	private void shouldReturnStatus(HttpStatus status) throws Exception {
		result
		   .andExpect(status().is(status.value()));
	}
	
	public SELF ok_status_should_be_returned() throws Exception {
		shouldReturnStatus(OK);
		return self();
	}
	
	public SELF created_status_should_be_returned() throws Exception {
		shouldReturnStatus(CREATED);
		return self();
	}

	public SELF notFound_status_should_be_returned() throws Exception {
		shouldReturnStatus(NOT_FOUND);
		return self();
	}
	
	public SELF badRequest_status_should_be_returned() throws Exception {
		shouldReturnStatus(BAD_REQUEST);
		return self();
	}

	public void empty_body_should_be_returned() throws Exception {
		result
		   .andExpect(content().string(isEmptyOrNullString()));
	}
	
}
