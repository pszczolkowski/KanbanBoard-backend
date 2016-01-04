package pl.pszczolkowski.kanban.domain.boardhistory.snapshot;

import java.time.LocalDateTime;
import java.util.Map;

public class BoardHistorySnapshot {

	private final long id;
	private final long boardId;
	private final LocalDateTime date;
	private final Map<String, Integer> columnSizes;
	
	public BoardHistorySnapshot(long id, long boardId, LocalDateTime date, Map<String, Integer> columnSizes) {
		this.id = id;
		this.boardId = boardId;
		this.date = date;
		this.columnSizes = columnSizes;
	}

	public long getId() {
		return id;
	}

	public long getBoardId() {
		return boardId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public Map<String, Integer> getColumnSizes() {
		return columnSizes;
	}
	
}
