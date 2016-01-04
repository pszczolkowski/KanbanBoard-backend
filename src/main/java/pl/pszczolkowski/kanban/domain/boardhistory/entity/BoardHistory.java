package pl.pszczolkowski.kanban.domain.boardhistory.entity;

import static java.util.Collections.unmodifiableMap;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import pl.pszczolkowski.kanban.config.persistance.converter.LocalDateTimePersistenceConverter;
import pl.pszczolkowski.kanban.domain.boardhistory.snapshot.BoardHistorySnapshot;
import pl.pszczolkowski.kanban.shared.exception.EntityInStateNewException;

@Entity
public class BoardHistory {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;
	
	@NotNull
	@Min(1)
	private long boardId;
	
	@NotNull
	private Timestamp date;
	
	@NotNull
	@ElementCollection(fetch = EAGER)
	private final Map<String, Integer> columnSizes = new HashMap<>();
	
	protected BoardHistory() {}
	
	public BoardHistory(long boardId, Map<String, Integer> columnSizes, LocalDate date) {
		this.boardId = boardId;
		this.columnSizes.putAll(columnSizes);
		this.date = LocalDateTimePersistenceConverter.convertToDatabaseColumnValue(date.atStartOfDay());
	}
	
	public BoardHistorySnapshot toSnapshot() {
		if (id == null) {
			throw new EntityInStateNewException();
		}
		
		LocalDateTime dateExport = LocalDateTimePersistenceConverter.convertToEntityAttributeValue(date);
		
		return new BoardHistorySnapshot(id, boardId, dateExport, unmodifiableMap(columnSizes));
	}

	public void update(Map<String, Integer> columnSizes) {
		this.columnSizes.putAll(columnSizes);
	}
	
}
