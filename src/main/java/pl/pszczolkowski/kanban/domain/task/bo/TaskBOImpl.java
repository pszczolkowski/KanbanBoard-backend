package pl.pszczolkowski.kanban.domain.task.bo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.task.entity.Column;
import pl.pszczolkowski.kanban.domain.task.entity.Task;
import pl.pszczolkowski.kanban.domain.task.entity.TaskPriority;
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
	public TaskSnapshot create(long columnId, String title, String description, Long assigneeId, Long labelId, TaskPriority taskPriority) {
		Column column = columnRepository.findOne(columnId);
		Optional<Integer> maxTaskIdOnBoard = taskRepository.findMaxTaskIdOnBoard(column.toSnapshot().getBoardId());
		
		Task task = new Task(column, maxTaskIdOnBoard.orElse(0) + 1, title, description, assigneeId, labelId, taskPriority);
		task = taskRepository.save(task);
		column.addTask(task);
		columnRepository.save(column);
		
		LOGGER.info("Task <{}> created on column <{}>", title, columnId);
		
		return task.toSnapshot();
	}

	private void moveTask(Task task, int position) {
		TaskSnapshot taskSnapshot = task.toSnapshot();
		Column column = columnRepository.findOne(taskSnapshot.getColumnId());
		
		if (position >= column.toSnapshot().getTasks().size()) {
			throw new IllegalArgumentException("Given position exceeds number of tasks in column");
		}
		
		column.moveTask(taskSnapshot.getId(), position);
		columnRepository.save(column);
	}
	
	@Override
	public void move(long taskId, int position) {
		Task task = taskRepository.findOne(taskId);
		moveTask(task, position);
	}
	
	@Override
	public void move(long taskId, Long columnId, int position) {
		if (columnId == null) {
			move(taskId, position);
			return;
		}
		
		Task task = taskRepository.findOne(taskId);
		
		TaskSnapshot taskSnapshot = task.toSnapshot();
		if (taskSnapshot.getColumnId() == columnId) {
			moveTask(task, position);
		} else {
			Column columnContainingTask = columnRepository.findOne(taskSnapshot.getColumnId());
			Column column = columnRepository.findOne(columnId);
			
			columnContainingTask.removeTask(task);
			column.addTask(task, position);
			task.moveTo(column);
			
			columnRepository.save(columnContainingTask);
			columnRepository.save(column);
		}
	}

	@Override
	public void edit(Long taskId, String title, String description, TaskPriority taskPriority) {
		Task task = taskRepository.findOne(taskId);
		TaskSnapshot taskSnapshot = task.toSnapshot();
		
		if (!title.equals(taskSnapshot.getTitle()) ||
				!Objects.equals(description, taskSnapshot.getDescription()) ||
				taskPriority != taskSnapshot.getPriority()) {
			task.edit(title, description, taskPriority);
			taskRepository.save(task);
		}
	}

	@Override
	public void detachLabelFromTasksInBoard(Long labelId) {
		List<Task> tasks = taskRepository.findByLabelId(labelId);
		tasks.stream()
			.forEach(t -> t.removeLabel());
		
		taskRepository.save(tasks);
	}

	@Override
	public void assignLabel(Long taskId, Long labelId) {
		Task task = taskRepository.findOne(taskId);
		
		task.setLabelId(labelId);
		taskRepository.save(task);
	}

	@Override
	public void assignUser(Long taskId, Long assigneeId) {
		Task task = taskRepository.findOne(taskId);
		
		task.assignUser(assigneeId);
		taskRepository.save(task);
	}

}
