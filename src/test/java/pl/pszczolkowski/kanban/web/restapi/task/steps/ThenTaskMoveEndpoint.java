package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.List;

import org.junit.Assert;

import com.tngtech.jgiven.annotation.ExpectedScenarioState;

import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.ThenStage;

public class ThenTaskMoveEndpoint extends ThenStage<ThenTaskMoveEndpoint> {

	@ExpectedScenarioState
	private List<TaskSnapshot> taskSnapshots;
	@ExpectedScenarioState
	private ColumnSnapshotFinder columnSnapshotFinder;
	
	public ThenTaskMoveEndpoint task_should_be_moved() {
		ColumnSnapshot columnSnapshot = columnSnapshotFinder.findById(taskSnapshots.get(0).getColumnId());
		
		TaskSnapshot firstTaskSnapshot = columnSnapshot.getTasks().get(0);
		TaskSnapshot lastTaskSnapshot = columnSnapshot.getTasks().get(columnSnapshot.getTasks().size() - 1);
		Assert.assertThat(firstTaskSnapshot.getId(), is(not(equalTo(taskSnapshots.get(0).getId()))));
		Assert.assertThat(lastTaskSnapshot.getId(), is(equalTo(taskSnapshots.get(0).getId())));
		
		return this;
	}

}
