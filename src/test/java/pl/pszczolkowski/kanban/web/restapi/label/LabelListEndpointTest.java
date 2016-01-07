package pl.pszczolkowski.kanban.web.restapi.label;

import static pl.pszczolkowski.kanban.util.Cleaner.clearBoards;
import static pl.pszczolkowski.kanban.util.Cleaner.clearLabels;
import static pl.pszczolkowski.kanban.util.Cleaner.clearUsers;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.tngtech.jgiven.junit.ScenarioTest;

import pl.pszczolkowski.kanban.Application;
import pl.pszczolkowski.kanban.web.restapi.label.steps.GivenLabelListEndpoint;
import pl.pszczolkowski.kanban.web.restapi.label.steps.ThenLabelListEndpoint;
import pl.pszczolkowski.kanban.web.restapi.shared.WhenStage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class LabelListEndpointTest
		extends ScenarioTest<GivenLabelListEndpoint, WhenStage, ThenLabelListEndpoint> {

	@After
	public void tearDown() {
		clearLabels();
		clearBoards();
		clearUsers();
	}
	
	@Test
	public void should_return_list_of_labels_in_requested_board() throws Exception {
		given().a_board()
			.and().some_labels()
			.and().a_request();
		when().request_is_invoked();
		then().list_of_labels_should_be_returned();
	}
	
	@Test
	public void should_return_badRequest_if_user_has_no_access_to_requested_board() throws Exception {
		given().a_board_that_user_has_no_access_to()
			.and().some_labels()
			.and().a_request();
		when().request_is_invoked();
		then().badRequest_status_should_be_returned();
	}
	
}
