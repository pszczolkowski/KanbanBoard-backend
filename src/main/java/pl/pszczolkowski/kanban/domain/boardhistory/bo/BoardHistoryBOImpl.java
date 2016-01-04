package pl.pszczolkowski.kanban.domain.boardhistory.bo;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.boardhistory.entity.BoardHistory;
import pl.pszczolkowski.kanban.domain.boardhistory.repository.BoardHistoryRepository;
import pl.pszczolkowski.kanban.domain.boardhistory.snapshot.BoardHistorySnapshot;
import pl.pszczolkowski.kanban.shared.annotations.BusinessObject;

@BusinessObject
public class BoardHistoryBOImpl implements BoardHistoryBO {

	private final BoardHistoryRepository boardHistoryRepository;
	
	@Autowired
	public BoardHistoryBOImpl(BoardHistoryRepository boardHistoryRepository) {
		this.boardHistoryRepository = boardHistoryRepository;
	}

	@Override
	public BoardHistorySnapshot save(long boardId, LocalDate date, Map<String, Integer> columnSizes) {
		Timestamp dateAsTimestamp = Timestamp.valueOf(date.atStartOfDay());
		BoardHistory boardHistory = boardHistoryRepository.findByBoardIdAndDate(boardId, dateAsTimestamp);
		
		if (boardHistory == null) {
			boardHistory = new BoardHistory(boardId, columnSizes, date);
		} else {
			boardHistory.update(columnSizes);
		}
		
		boardHistory = boardHistoryRepository.save(boardHistory);
		return boardHistory.toSnapshot();
	}

}
