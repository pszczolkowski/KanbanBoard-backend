package pl.pszczolkowski.kanban.web.restapi.task;

import static pl.pszczolkowski.kanban.util.Cleaner.clearBoards;
import static pl.pszczolkowski.kanban.util.Cleaner.clearColumns;
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
import pl.pszczolkowski.kanban.web.restapi.shared.WhenStage;
import pl.pszczolkowski.kanban.web.restapi.task.steps.GivenColumnGetEndpoint;
import pl.pszczolkowski.kanban.web.restapi.task.steps.ThenColumnGetEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ColumnGetEndpointTest
		extends ScenarioTest<GivenColumnGetEndpoint, WhenStage, ThenColumnGetEndpoint> {

	@After
	public void tearDown() {
		clearColumns();
		clearBoards();
		clearUsers();
	}
	
	@Test
	public void should_return_requested_column() throws Exception {
		given().a_board()
			.and().a_column()
			.and().a_request();
		when().request_is_invoked();
		then().ok_status_should_be_returned()
			.and().requested_column_should_be_returned();
	}
	
	@Test
	public void should_return_notFound_if_user_has_no_access_to_requested_column() throws Exception {
		given().a_board_that_user_has_no_access_to()
			.and().a_column()
			.and().a_request();
		when().request_is_invoked();
		then().notFound_status_should_be_returned();
	}
	
	@Test
	public void should_return_notFound_if_column_does_not_exist() throws Exception {
		given().a_request_with_not_existing_column_id();
		when().request_is_invoked();
		then().notFound_status_should_be_returned();
	}
	
}
