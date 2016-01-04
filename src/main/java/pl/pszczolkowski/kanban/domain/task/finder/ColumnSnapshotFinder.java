package pl.pszczolkowski.kanban.domain.task.finder;

import java.util.List;
import java.util.Map;

import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;

public interface ColumnSnapshotFinder {

	List<ColumnSnapshot> findByBoardId(long boardId);
	
	ColumnSnapshot findById(long columnId);

	ColumnSnapshot findByNameAndBoardId(String name, long boardId);
	
	Map<Long, List<ColumnSnapshot>> findAllGroupedByBoardId();
	
}
