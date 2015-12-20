package pl.pszczolkowski.kanban.domain.task.bo;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.task.entity.Column;
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
	public ColumnSnapshot add(long boardId, String name, int workInProgressLimit) {
		if (columnAlreadyExists(boardId, name)) {
			throw new IllegalArgumentException("Column with name <" + name + "> already exist in board with id <" + boardId + ">");
		}
		
		Optional<Integer> maxPositionOnBoard = columnRepository.findMaxPositionOnBoard(boardId);
		
		Column column = new Column(boardId, name, maxPositionOnBoard.orElse(0) + 1, workInProgressLimit);
		column = columnRepository.save(column);
		
		LOGGER.info("Column <{}> added to board with id <{}>",
				name,
				boardId);
		
		return column.toSnapshot();
	}

	private boolean columnAlreadyExists(long boardId, String name) {
		return columnRepository.findByBoardIdAndName(boardId, name) != null;
	}
	

}