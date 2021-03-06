package pl.pszczolkowski.kanban.domain.task.bo;

import pl.pszczolkowski.kanban.domain.task.entity.WorkInProgressLimitType;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;

public interface ColumnBO {

	ColumnSnapshot add(long boardId, String name, Integer workInProgressLimit, WorkInProgressLimitType workInProgressLimitType);

	void move(long columnId, int position);

	void delete(Long columnId, Long columnToMoveTasksId);

	void edit(Long columnId, String name, Integer workInProgressLimit, WorkInProgressLimitType workInProgressLimitType);

	void deleteFromBoard(long boardId);
	
}
