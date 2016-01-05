package pl.pszczolkowski.kanban.domain.task.bo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.task.entity.Column;
import pl.pszczolkowski.kanban.domain.task.entity.Task;
import pl.pszczolkowski.kanban.domain.task.entity.WorkInProgressLimitType;
import pl.pszczolkowski.kanban.domain.task.repository.ColumnRepository;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.BusinessObject;

@BusinessObject
public class ColumnBOImpl implements ColumnBO {

	private final static Logger LOGGER = LoggerFactory.getLogger(ColumnBOImpl.class);
	
	private final ColumnRepository columnRepository;
	
	@Autowired
	public ColumnBOImpl(ColumnRepository columnRepository) {
		this.columnRepository = columnRepository;
	}

	@Override
	public ColumnSnapshot add(long boardId, String name, Integer workInProgressLimit, WorkInProgressLimitType workInProgressLimitType) {
		if (columnAlreadyExists(boardId, name)) {
			throw new IllegalArgumentException("Column with name <" + name + "> already exist in board with id <" + boardId + ">");
		}
		
		Optional<Integer> maxPositionOnBoard = columnRepository.findMaxPositionOnBoard(boardId);
		
		Column column = new Column(boardId, name, maxPositionOnBoard.orElse(0) + 1, workInProgressLimit, workInProgressLimitType);
		column = columnRepository.save(column);
		
		LOGGER.info("Column <{}> added to board with id <{}>",
				name,
				boardId);
		
		return column.toSnapshot();
	}

	private boolean columnAlreadyExists(long boardId, String name) {
		return columnRepository.findByBoardIdAndName(boardId, name) != null;
	}

	@Override
	public void move(long columnId, int position) {
		Column column = columnRepository.findOne(columnId);
		List<Column> columns = columnRepository.findByBoardIdOrderByPosition(column.toSnapshot().getBoardId());
		
		if (position >= columns.size()) {
			throw new IllegalArgumentException("Invalid position");
		}
		
		int index = -1;
		for (int i = 0; i < columns.size(); i++) {
			if (columns.get(i).getId() == column.getId()) {
				index = i;
				break;
			}
		}
		
		column = columns.remove(index);
		columns.add(position, column);
		
		for (int j = 0; j < columns.size(); j++) {
			columns.get(j).setPosition(j);
		}
		
		columnRepository.save(columns);
	}

	@Override
	public void delete(Long columnId, Long columnToMoveTasksId) {
		Column column = columnRepository.findOne(columnId);
		ColumnSnapshot columnSnapshot = column.toSnapshot();
		if (columnSnapshot.getTasks().size() > 0) {
			Column columnToMoveTasks = columnRepository.findOne(columnToMoveTasksId);
			if (columnToMoveTasks.toSnapshot().getBoardId() != columnSnapshot.getBoardId()) {
				throw new IllegalArgumentException("Column to move tasks has to be in the same board as the deleting one");
			}
			
			for (Task task : column.getTasks()) {
				column.removeTask(task);
				columnToMoveTasks.addTask(task);
			}

			columnRepository.save(column);
			columnRepository.save(columnToMoveTasks);
		}
		
		columnRepository.delete(column);;
	}

	@Override
	public void edit(Long columnId, String name, Integer workInProgressLimit, WorkInProgressLimitType workInProgressLimitType) {
		Column column = columnRepository.findOne(columnId);
		ColumnSnapshot columnSnapshot = column.toSnapshot();
		
		if (!columnSnapshot.getName().equals(name) ||
				!Objects.equals(columnSnapshot.getWorkInProgressLimit(), workInProgressLimit)) {
			column.edit(name, workInProgressLimit, workInProgressLimitType);
			columnRepository.save(column);
		}
	}

	@Override
	public void deleteFromBoard(long boardId) {
		columnRepository.deleteByBoardId(boardId);
	}

}
