package pl.pszczolkowski.kanban.builder;

import static pl.pszczolkowski.kanban.builder.ColumnBuilder.aColumn;
import static pl.pszczolkowski.kanban.domain.task.entity.TaskPriority.LOW;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.kanban.domain.task.entity.Column;
import pl.pszczolkowski.kanban.domain.task.entity.Task;
import pl.pszczolkowski.kanban.domain.task.entity.TaskPriority;
import pl.pszczolkowski.kanban.domain.task.repository.ColumnRepository;
import pl.pszczolkowski.kanban.domain.task.repository.TaskRepository;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;

@Component
public class TaskBuilder implements ApplicationContextAware {

	private static final String CLAZZ = TaskBuilder.class.getSimpleName();
	
	private static TaskRepository taskRepository;
	private static ColumnRepository columnRepository;
	
	private ColumnSnapshot columnSnapshot = null;
	private int idOnBoard = 1;
	private String title = CLAZZ;
	private String description = null; 
	private Long assigneeId = null; 
	private Long labelId = null; 
	private TaskPriority priority = LOW;
	private float size = 1;
	
	public TaskBuilder withColumn(ColumnSnapshot columnSnapshot) {
		this.columnSnapshot = columnSnapshot;
		return this;
	}
	
	public TaskBuilder withIdOnBoard(int idOnBoard) {
		this.idOnBoard = idOnBoard;
		return this;
	}
	
	public TaskBuilder withTitle(String title) {
		this.title = title;
		return this;
	}
	
	public TaskBuilder withDescription(String description) {
		this.description = description;
		return this;
	}
	
	public TaskBuilder withAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
		return this;
	}
	
	public TaskBuilder withLabelId(Long labelId) {
		this.labelId = labelId;
		return this;
	}
	
	public TaskBuilder withPriority(TaskPriority priority) {
		this.priority = priority;
		return this;
	}
	
	public TaskBuilder withSize(float size) {
		this.size = size;
		return this;
	}
	
	public TaskSnapshot build() {
		if (columnSnapshot == null) {
			columnSnapshot = aColumn().build();
		}
		
		Column column = columnRepository.findOne(columnSnapshot.getId());
		Task task = new Task(column, idOnBoard, title, description, assigneeId, labelId, priority, size);
		
		task = taskRepository.save(task);
		return task.toSnapshot();
	}
	
	public static TaskBuilder aTask() {
		return new TaskBuilder();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		taskRepository = applicationContext.getBean(TaskRepository.class);
		columnRepository = applicationContext.getBean(ColumnRepository.class);
	}
	
}
