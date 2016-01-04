package pl.pszczolkowski.kanban.domain.boardhistory.bo;

import java.time.LocalDate;
import java.util.Map;

import pl.pszczolkowski.kanban.domain.boardhistory.snapshot.BoardHistorySnapshot;

public interface BoardHistoryBO {

	BoardHistorySnapshot save(long boardId, LocalDate date, Map<String, Integer> columnSizes);
	
}
