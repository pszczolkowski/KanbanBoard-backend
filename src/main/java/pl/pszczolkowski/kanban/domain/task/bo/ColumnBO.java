package pl.pszczolkowski.kanban.domain.task.bo;

import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;

public interface ColumnBO {

	ColumnSnapshot add(long boardId, String name, int workInProgressLimit);
	
}
