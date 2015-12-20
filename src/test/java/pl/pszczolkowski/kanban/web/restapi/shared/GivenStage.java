package pl.pszczolkowski.kanban.web.restapi.shared;

import static com.tngtech.jgiven.annotation.ScenarioState.Resolution.NAME;
import static pl.pszczolkowski.kanban.builder.UserBuilder.anUser;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeScenario;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

public abstract class GivenStage<SELF extends GivenStage<?>> extends Stage<SELF> {

	private static final String MOCKED_USER_LOGIN;
	
	static {
		try {
			MOCKED_USER_LOGIN = (String) WithMockUser.class.getMethod("value").getDefaultValue();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	@ProvidedScenarioState
	private MockHttpServletRequestBuilder request;
	@ProvidedScenarioState(resolution = NAME)
	private UserSnapshot loggedUserSnapshot;

	@BeforeScenario
	public void setUp() {
		loggedUserSnapshot = anUser()
			.withLogin(MOCKED_USER_LOGIN)
			.build();
	}
	
	protected void prepareRequest(MockHttpServletRequestBuilder request) {
		this.request = request;
	}
	
	protected UserSnapshot getLoggedUser() {
		return loggedUserSnapshot;
	}
	
}
