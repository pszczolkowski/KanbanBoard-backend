package pl.pszczolkowski.kanban.domain.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.pszczolkowski.kanban.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	List<Board> findByOwnerId(long ownerId);

	Board findByIdAndOwnerId(long boardId, long ownerId);

	List<Board> findByNameAndOwnerId(String name, long ownerId);

}
