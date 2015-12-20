package pl.pszczolkowski.kanban.domain.board.finder;

import java.util.List;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;

public interface BoardSnapshotFinder {

	List<BoardSnapshot> findByOwnerId(long ownerId);
	
	BoardSnapshot findById(long boardId);

	BoardSnapshot findByIdAndOwnerId(long boardId, Long ownerId);

	List<BoardSnapshot> findByNameAndOWnerId(String name, Long id);

}
