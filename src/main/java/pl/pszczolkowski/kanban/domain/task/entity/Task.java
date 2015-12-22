package pl.pszczolkowski.kanban.domain.task.entity;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import pl.pszczolkowski.kanban.config.persistance.converter.LocalDateTimePersistenceConverter;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.shared.exception.EntityInStateNewException;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;
	
	@NotNull
	@Min(1)
	private int idOnBoard;
	
	@NotNull
	@Min(1)
	private long boardId;
	
	@NotNull
	@Size(min = 2, max = 500)
	private String title;
	
	@Size(min = 1, max = 10000)
	private String description;

	@Min(1)
	private Long assigneeId;
	
	@NotNull
	@Min(0)
	private int position;
	
	@Min(1)
	private Long labelId;
	
	@NotNull
	private Timestamp createdAt;
	
	@NotNull
	@ManyToOne(fetch = EAGER)
	private Column column;
	
	@Version
	private long version;
	
	protected Task() {}

	public Task(Column column, int idOnBoard, String title, String description, Long assigneeId, Long labelId) {
		this.column = column;
		this.idOnBoard = idOnBoard;
		this.labelId = labelId;
		this.boardId = column.getBoardId();
		this.title = title;
		this.description = description;
		this.assigneeId = assigneeId;
		this.position = column.countTasks();
		this.createdAt = LocalDateTimePersistenceConverter.convertToDatabaseColumnValue(LocalDateTime.now());
	}

	public TaskSnapshot toSnapshot() {
		if (id == null) {
			throw new EntityInStateNewException();
		}
		
		LocalDateTime createdAtExport = LocalDateTimePersistenceConverter.convertToEntityAttributeValue(createdAt);
		return new TaskSnapshot(id, idOnBoard, boardId, title, description, assigneeId, position, labelId, createdAtExport, column.getId());
	}

	Long getId() {
		return id;
	}

	void setPosition(int position) {
		this.position = position;
	}

	public void moveTo(Column column) {
		this.column = column;
	}
	
}
