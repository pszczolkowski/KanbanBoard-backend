package pl.pszczolkowski.kanban.domain.task.bo;

import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;

public interface TaskBO {

	TaskSnapshot create(long columnId, String title, String description, Long assigneeId, Long labelId);

	void move(long taskId, int position);
	
	void move(long taskId, Long columnId, int position);

	void edit(Long id, String title, String description);

	void detachLabelFromTasksInBoard(Long labelId);

	void assignLabel(Long taskId, Long labelId);

	void assignUser(Long taskId, Long assigneeId);
	
}
