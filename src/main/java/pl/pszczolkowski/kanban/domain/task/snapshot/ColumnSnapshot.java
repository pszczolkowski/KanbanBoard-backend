package pl.pszczolkowski.kanban.domain.task.snapshot;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import pl.pszczolkowski.kanban.domain.task.entity.WorkInProgressLimitType;

public class ColumnSnapshot {

	private final long id;
	private final String name;
	private final long boardId;
	private final int position;
	private final Integer workInProgressLimit;
	private final WorkInProgressLimitType workInProgressLimitType;
	private final List<TaskSnapshot> tasks = new ArrayList<>();
	
	public ColumnSnapshot(long id, String name, long boardId, int position, Integer workInProgressLimit,
			WorkInProgressLimitType workInProgressLimitType, List<TaskSnapshot> tasks) {
		this.id = id;
		this.name = name;
		this.boardId = boardId;
		this.position = position;
		this.workInProgressLimit = workInProgressLimit;
		this.workInProgressLimitType = workInProgressLimitType;
		this.tasks.addAll(tasks);
	}

	public long getId() {
		return id;
	}

	public int getPosition() {
		return position;
	}

	public Integer getWorkInProgressLimit() {
		return workInProgressLimit;
	}

	public String getName() {
		return name;
	}

	public long getBoardId() {
		return boardId;
	}

	public WorkInProgressLimitType getWorkInProgressLimitType() {
		return workInProgressLimitType;
	}

	public List<TaskSnapshot> getTasks() {
		return unmodifiableList(tasks);
	}
	
}
