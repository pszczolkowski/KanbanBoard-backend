package pl.pszczolkowski.kanban.domain.task.snapshot;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class ColumnSnapshot {

	private final long id;
	private final String name;
	private final long boardId;
	private final int position;
	private final int workInProgressLimit;
	private final List<TaskSnapshot> tasks = new ArrayList<>();
	
	public ColumnSnapshot(long id, String name, long boardId, int position, int workInProgressLimit, List<TaskSnapshot> tasks) {
		this.id = id;
		this.name = name;
		this.boardId = boardId;
		this.position = position;
		this.workInProgressLimit = workInProgressLimit;
		this.tasks.addAll(tasks);
	}

	public long getId() {
		return id;
	}

	public int getPosition() {
		return position;
	}

	public int getWorkInProgressLimit() {
		return workInProgressLimit;
	}

	public String getName() {
		return name;
	}

	public long getBoardId() {
		return boardId;
	}

	public List<TaskSnapshot> getTasks() {
		return unmodifiableList(tasks);
	}
	
}
