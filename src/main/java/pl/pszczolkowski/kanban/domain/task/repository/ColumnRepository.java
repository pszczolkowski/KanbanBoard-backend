package pl.pszczolkowski.kanban.domain.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.pszczolkowski.kanban.domain.task.entity.Column;

public interface ColumnRepository extends JpaRepository<Column, Long> {

	Column findByBoardIdAndName(long boardId, String name);
	
	@Query("SELECT Max(c.position) FROM Column c WHERE c.boardId = :boardId")
	Optional<Integer> findMaxPositionOnBoard(@Param("boardId") long boardId);

	List<Column> findByBoardId(long boardId);

	List<Column> findByBoardIdOrderByPosition(long boardId);

}
