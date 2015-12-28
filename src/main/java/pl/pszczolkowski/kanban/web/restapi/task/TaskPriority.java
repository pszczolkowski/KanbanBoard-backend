package pl.pszczolkowski.kanban.web.restapi.task;

public enum TaskPriority {

	LOW, MEDIUM, HIGH;
	
	public static TaskPriority from(pl.pszczolkowski.kanban.domain.task.entity.TaskPriority priority) {
		switch (priority) {
			case LOW:
				return LOW;
			case MEDIUM:
				return MEDIUM;
			case HIGH:
				return HIGH;
			default:
				throw new IllegalArgumentException("Task priority <" + priority + "> not supported");
		}
	}
	
	public pl.pszczolkowski.kanban.domain.task.entity.TaskPriority toDomain() {
		switch (this) {
			case LOW:
				return pl.pszczolkowski.kanban.domain.task.entity.TaskPriority.LOW;
			case MEDIUM:
				return pl.pszczolkowski.kanban.domain.task.entity.TaskPriority.MEDIUM;
			case HIGH:
				return pl.pszczolkowski.kanban.domain.task.entity.TaskPriority.HIGH;
			default:
				throw new IllegalArgumentException("Task priority <" + this + "> not supported");
		}
	}
	
}
