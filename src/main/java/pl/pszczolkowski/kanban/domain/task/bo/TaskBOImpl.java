package pl.pszczolkowski.kanban.domain.task.bo;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.task.entity.Column;
import pl.pszczolkowski.kanban.domain.task.entity.Task;
import pl.pszczolkowski.kanban.domain.task.repository.ColumnRepository;
import pl.pszczolkowski.kanban.domain.task.repository.TaskRepository;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.BusinessObject;

@BusinessObject
public class TaskBOImpl implements TaskBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskBOImpl.class);
	
	private final TaskRepository taskRepository;
	private final ColumnRepository columnRepository;
	
	@Autowired
	public TaskBOImpl(TaskRepository taskRepository, ColumnRepository columnRepository) {
		this.taskRepository = taskRepository;
		this.columnRepository = columnRepository;
	}

	@Override
	public TaskSnapshot create(long columnId, String title, String description, Long assigneeId, Long labelId) {
		Column column = columnRepository.findOne(columnId);
		Optional<Integer> maxTaskIdOnBoard = taskRepository.findMaxTaskIdOnBoard(column.toSnapshot().getBoardId());
		
		Task task = new Task(column, maxTaskIdOnBoard.orElse(0) + 1, title, description, assigneeId, labelId);
		task = taskRepository.save(task);
		column.addTask(task);
		columnRepository.save(column);
		
		LOGGER.info("Task <{}> created on column <{}>", title, columnId);
		
		return task.toSnapshot();
	}

}
