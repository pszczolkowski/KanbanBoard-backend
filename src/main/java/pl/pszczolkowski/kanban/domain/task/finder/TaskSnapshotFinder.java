package pl.pszczolkowski.kanban.domain.task.finder;

import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;

public interface TaskSnapshotFinder {

	TaskSnapshot findById(long taskId);
	
}
