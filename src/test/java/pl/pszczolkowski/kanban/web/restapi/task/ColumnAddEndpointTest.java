package pl.pszczolkowski.kanban.web.restapi.task;

import static pl.pszczolkowski.kanban.util.Cleaner.clearBoards;
import static pl.pszczolkowski.kanban.util.Cleaner.clearUsers;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;

import pl.pszczolkowski.kanban.Application;
import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.util.Cleaner;
import pl.pszczolkowski.kanban.web.restapi.shared.WhenStage;
import pl.pszczolkowski.kanban.web.restapi.task.steps.GivenColumnAddEndpoint;
import pl.pszczolkowski.kanban.web.restapi.task.steps.ThenColumnAddEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ColumnAddEndpointTest
		extends ScenarioTest<GivenColumnAddEndpoint, WhenStage, ThenColumnAddEndpoint> {

	@Autowired
	@ProvidedScenarioState
	private ColumnSnapshotFinder columnSnapshotFinder;
	
	@After
	public void tearDown() {
		Cleaner.clearColumns();
		clearBoards();
		clearUsers();
	}
	
	@Test
	public void should_add_new_column() throws Exception {
		given().a_board_that_logged_user_has_access_to()
			.and().a_request();
		when().request_is_invoked();
		then().created_status_should_be_returned()
			.and().column_should_be_created()
			.and().created_column_should_be_returned();
	}
	
	@Test
	public void should_return_bad_request_if_board_does_not_exist() throws Exception {
		given().a_request_with_not_existing_board_id();
		when().request_is_invoked();
		then().badRequest_status_should_be_returned();
	}
	
	@Test
	public void should_return_bad_request_if_user_has_no_access_to_board() throws Exception {
		given().a_board_that_logged_user_has_no_access_to()
			.and().a_request();
		when().request_is_invoked();
		then().badRequest_status_should_be_returned();
	}
	
	@Test
	public void should_return_bad_request_if_column_with_given_name_already_exist_in_board() throws Exception {
		given().a_board_that_logged_user_has_access_to()
			.and().a_column()
			.and().a_request_to_add_column_with_the_same_name();
		when().request_is_invoked();
		then().badRequest_status_should_be_returned();
	}

}
