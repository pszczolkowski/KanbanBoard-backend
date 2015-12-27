package pl.pszczolkowski.kanban.domain.board.finder;

import java.util.List;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;

public interface BoardSnapshotFinder {

	List<BoardSnapshot> findByMemberId(long memberId);
	
	BoardSnapshot findById(long boardId);

	List<BoardSnapshot> findByNameAndMemberId(String name, long memberId);

}
