package pl.pszczolkowski.kanban.web.restapi.board;

import static pl.pszczolkowski.kanban.util.Cleaner.clearBoards;
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
import pl.pszczolkowski.kanban.web.restapi.board.steps.GivenBoardListEndpoint;
import pl.pszczolkowski.kanban.web.restapi.board.steps.ThenBoardListEndpoint;
import pl.pszczolkowski.kanban.web.restapi.shared.WhenStage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class BoardListEndpointTest
		extends ScenarioTest<GivenBoardListEndpoint, WhenStage, ThenBoardListEndpoint> {

	@After
	public void tearDown() {
		clearBoards();
		clearUsers();
	}
	
	@Test
	public void should_return_all_boards_that_logged_user_has_access_to() throws Exception {
		given().some_boards()
			.and().a_request();
		when().request_is_invoked();
		then().ok_status_should_be_returned()
			.and().list_of_boards_should_be_returned();
	}

}
