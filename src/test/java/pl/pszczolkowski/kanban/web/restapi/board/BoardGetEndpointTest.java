package pl.pszczolkowski.kanban.web.restapi.board;

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
import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.web.restapi.board.steps.GivenBoardGetEndpoint;
import pl.pszczolkowski.kanban.web.restapi.board.steps.ThenBoardGetEndpoint;
import pl.pszczolkowski.kanban.web.restapi.shared.WhenStage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class BoardGetEndpointTest
		extends ScenarioTest<GivenBoardGetEndpoint, WhenStage, ThenBoardGetEndpoint> {

	@Autowired
	@ProvidedScenarioState
	private BoardSnapshotFinder boardSnapshotFinder;
	
	@After
	public void tearDown() {
		clearBoards();
		clearUsers();
	}
	
	@Test
	public void should_return_requested_board() throws Exception {
		given().a_board_for_logged_user()
			.and().a_request();
		when().request_is_invoked();
		then().ok_status_should_be_returned()
			.and().requested_board_should_be_returned();
	}
	
	@Test
	public void should_return_notFound_if_requested_board_doesnt_exist() throws Exception {
		given().a_request_to_get_not_existing_board();
		when().request_is_invoked();
		then().notFound_status_should_be_returned()
			.and().empty_body_should_be_returned();
	}
	
	@Test
	public void should_return_notFound_if_user_has_no_access_to_requested_board() throws Exception {
		given().a_board_that_logged_user_has_no_access_to()
			.and().a_request();
		when().request_is_invoked();
		then().notFound_status_should_be_returned()
			.and().empty_body_should_be_returned();
	}

}
