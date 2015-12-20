package pl.pszczolkowski.kanban.web.restapi.shared;

import static com.tngtech.jgiven.annotation.ScenarioState.Resolution.NAME;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static pl.pszczolkowski.kanban.util.OAuthHelper.bearerTokenForUser;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

@Component
public class WhenStage extends Stage<WhenStage> implements ApplicationContextAware {

	private static WebApplicationContext context;
	
	@ProvidedScenarioState
	private ResultActions result;

	@ExpectedScenarioState
	private MockHttpServletRequestBuilder request;
	@ExpectedScenarioState(resolution = NAME)
	private UserSnapshot loggedUserSnapshot;

	private MockMvc mockMvc;

	@BeforeStage
	public void setUp() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}

	public void request_is_invoked() throws Exception {
		RequestPostProcessor bearerToken = bearerTokenForUser(loggedUserSnapshot);
		result = mockMvc.perform(request.with(bearerToken));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = (WebApplicationContext) applicationContext;
	}

}
