package pl.pszczolkowski.kanban.domain.task.entity;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.TaskSnapshot;
import pl.pszczolkowski.kanban.shared.exception.EntityInStateNewException;

@Entity
@Table(name = "board_column")
public class Column {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;
	
	@NotEmpty
	@Size(max = 20)
	private String name;
	
	@NotNull
	@Min(0)
	private long boardId;
	
	@NotNull
	@Min(0)
	private int position;
	
	@Min(0)
	private int workInProgressLimit;
	
	@Version
	private long version;
	
	@OrderBy("position")
	@OneToMany(fetch = FetchType.EAGER, cascade = ALL, orphanRemoval = true, mappedBy = "column")
	private final List<Task> tasks = new ArrayList<>();
	
	protected Column() {}
	
	public Column(long boardId, String name, int position, int workInProgressLimit) {
		this.boardId = boardId;
		this.name = name;
		this.position = position;
		this.workInProgressLimit = workInProgressLimit;
	}
	
	public ColumnSnapshot toSnapshot() {
		if (id == null) {
			throw new EntityInStateNewException();
		}
		
		List<TaskSnapshot> taskSnapshots = tasks
			.stream()
			.map(Task::toSnapshot)
			.collect(toList());
		
		return new ColumnSnapshot(id, name, boardId, position, workInProgressLimit, taskSnapshots);
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}

	int countTasks() {
		return tasks.size();
	}

	long getBoardId() {
		return boardId;
	}

	long getId() {
		return id;
	}
	
}
