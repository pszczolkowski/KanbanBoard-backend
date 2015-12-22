package pl.pszczolkowski.kanban.domain.task.finder;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.task.entity.Task;
import pl.pszczolkowski.kanban.domain.task.repository.TaskRepository;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.SnapshotFinder;

@SnapshotFinder
public class TaskSnapshotFinderImpl implements TaskSnapshotFinder {

	private final TaskRepository taskRepository;
	
	@Autowired
	public TaskSnapshotFinderImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public TaskSnapshot findById(long taskId) {
		return taskRepository
			.findById(taskId)
			.map(Task::toSnapshot)
			.orElse(null);
	}
	
}
