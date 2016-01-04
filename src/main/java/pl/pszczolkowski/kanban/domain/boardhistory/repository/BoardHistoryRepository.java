package pl.pszczolkowski.kanban.domain.boardhistory.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.pszczolkowski.kanban.domain.boardhistory.entity.BoardHistory;

public interface BoardHistoryRepository extends JpaRepository<BoardHistory, Long> {

	BoardHistory findByBoardIdAndDate(long boardId, Timestamp date);

	List<BoardHistory> findByBoardIdOrderByDateAsc(long boardId);

}
