package pl.pszczolkowski.kanban.domain.boardhistory.finder;

import java.util.List;

import pl.pszczolkowski.kanban.domain.boardhistory.snapshot.BoardHistorySnapshot;

public interface BoardHistorySnapshotFinder {

	List<BoardHistorySnapshot> findByBoardId(long boardId);
	
}
