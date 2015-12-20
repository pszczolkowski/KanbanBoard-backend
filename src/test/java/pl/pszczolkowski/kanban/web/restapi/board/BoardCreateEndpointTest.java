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
import pl.pszczolkowski.kanban.web.restapi.board.steps.GivenBoardCreateEndpoint;
import pl.pszczolkowski.kanban.web.restapi.board.steps.ThenBoardCreateEndpoint;
import pl.pszczolkowski.kanban.web.restapi.shared.WhenStage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class BoardCreateEndpointTest
		extends ScenarioTest<GivenBoardCreateEndpoint, WhenStage, ThenBoardCreateEndpoint> {

	@Autowired
	@ProvidedScenarioState
	private BoardSnapshotFinder boardSnapshotFinder;
	
	@After
	public void tearDown() {
		clearBoards();
		clearUsers();
	}
	
	@Test
	public void should_create_new_board() throws Exception {
		given().a_request();
		when().request_is_invoked();
		then().created_status_should_be_returned()
			.and().board_should_be_created()
			.and().created_board_should_be_returned();
	}

}
