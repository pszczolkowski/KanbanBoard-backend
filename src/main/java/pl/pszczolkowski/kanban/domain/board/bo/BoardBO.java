package pl.pszczolkowski.kanban.domain.board.bo;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;

public interface BoardBO {

	BoardSnapshot create(String name, long ownerId);
	
}
