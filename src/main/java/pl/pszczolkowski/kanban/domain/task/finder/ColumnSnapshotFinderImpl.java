package pl.pszczolkowski.kanban.domain.task.finder;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.task.entity.Column;
import pl.pszczolkowski.kanban.domain.task.repository.ColumnRepository;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.SnapshotFinder;

@SnapshotFinder
public class ColumnSnapshotFinderImpl implements ColumnSnapshotFinder {

	private final ColumnRepository columnRepository;
	
	@Autowired
	public ColumnSnapshotFinderImpl(ColumnRepository columnRepository) {
		this.columnRepository = columnRepository;
	}

	@Override
	public List<ColumnSnapshot> findByBoardId(long boardId) {
		List<Column> columns = columnRepository.findByBoardIdOrderByPosition(boardId);
		return snapshotsFrom(columns);
	}

	private List<ColumnSnapshot> snapshotsFrom(List<Column> columns) {
		return columns.stream()
				.map(Column::toSnapshot)
				.collect(toList());
	}

	@Override
	public ColumnSnapshot findById(long columnId) {
		Column column = columnRepository.findOne(columnId);
		return column == null ? null : column.toSnapshot();
	}

	@Override
	public ColumnSnapshot findByNameAndBoardId(String name, long boardId) {
		Column column = columnRepository.findByBoardIdAndName(boardId, name);
		return column == null ? null : column.toSnapshot();
	}

	@Override
	public Map<Long, List<ColumnSnapshot>> findAllGroupedByBoardId() {
		return columnRepository
			.findAll()
			.stream()
			.map(Column::toSnapshot)
			.collect(groupingBy(ColumnSnapshot::getBoardId));
	}

}
