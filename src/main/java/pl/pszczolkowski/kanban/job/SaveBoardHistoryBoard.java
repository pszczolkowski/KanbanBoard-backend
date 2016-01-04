package pl.pszczolkowski.kanban.job;

import static java.util.stream.Collectors.toMap;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.kanban.domain.boardhistory.bo.BoardHistoryBO;
import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;

@Component
public class SaveBoardHistoryBoard {

	private static final long TWO_HOURS = 2 * 60 * 60 * 1000;

	private final ColumnSnapshotFinder columnSnapshotFinder;
	private final BoardHistoryBO boardHistoryBO;
	
	@Autowired
	public SaveBoardHistoryBoard(ColumnSnapshotFinder columnSnapshotFinder, BoardHistoryBO boardHistoryBO) {
		this.columnSnapshotFinder = columnSnapshotFinder;
		this.boardHistoryBO = boardHistoryBO;
	}
	
	@Scheduled(fixedRate = TWO_HOURS)
	public void save() {
		Map<Long, List<ColumnSnapshot>> columnSnapshots = columnSnapshotFinder.findAllGroupedByBoardId();
		for (Entry<Long, List<ColumnSnapshot>> entry : columnSnapshots.entrySet()) {
			Map<String, Integer> columnSizes = fetchColumnSizes(entry);
			
			boardHistoryBO.save(entry.getKey(), LocalDate.now(), columnSizes);
		}		
	}

	private Map<String, Integer> fetchColumnSizes(Entry<Long, List<ColumnSnapshot>> entry) {
		Map<String, Integer> columnSizes = entry.getValue()
			.stream()
			.collect(toMap(ColumnSnapshot::getName, c -> c.getTasks().size()));
		return columnSizes;
	}
	
}
