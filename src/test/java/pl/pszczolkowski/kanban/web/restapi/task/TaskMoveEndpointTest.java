package pl.pszczolkowski.kanban.web.restapi.task;

import static pl.pszczolkowski.kanban.util.Cleaner.cleanTasks;
import static pl.pszczolkowski.kanban.util.Cleaner.clearBoards;
import static pl.pszczolkowski.kanban.util.Cleaner.clearColumns;
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
import pl.pszczolkowski.kanban.web.restapi.shared.WhenStage;
import pl.pszczolkowski.kanban.web.restapi.task.steps.GivenTaskMoveEndpoint;
import pl.pszczolkowski.kanban.web.restapi.task.steps.ThenTaskMoveEndpoint;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class TaskMoveEndpointTest
		extends ScenarioTest<GivenTaskMoveEndpoint, WhenStage, ThenTaskMoveEndpoint> {

	@Autowired
	@ProvidedScenarioState
	private ColumnSnapshotFinder columnSnapshotFinder;
	
	@After
	public void tearDown() {
		cleanTasks();
		clearColumns();
		clearBoards();
		clearUsers();
	}
	
	@Test
	public void should_move_requested_task() throws Exception {
		given().a_board()
			.and().a_column()
			.and().some_tasks()
			.and().a_request();
		when().request_is_invoked();
		then().ok_status_should_be_returned()
			.and().task_should_be_moved();
	}
	
}
