package pl.pszczolkowski.kanban.domain.board.snapshot;

import java.time.LocalDateTime;

public class BoardSnapshot {

	private final long id;
	private final String name;
	private long ownerId;
	private LocalDateTime createdAt;
	
	public BoardSnapshot(long id, String name, long ownerId, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.ownerId = ownerId;
		this.createdAt = createdAt;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
}
