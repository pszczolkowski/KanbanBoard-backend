package pl.pszczolkowski.kanban.domain.board.snapshot;

import static java.util.Collections.unmodifiableList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardSnapshot {

	private final long id;
	private final String name;
	private final LocalDateTime createdAt;
	private final List<BoardMemberSnapshot> memberSnapshots = new ArrayList<>();
	
	public BoardSnapshot(long id, String name, LocalDateTime createdAt, List<BoardMemberSnapshot> memberSnapshots) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.memberSnapshots.addAll(memberSnapshots);
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<BoardMemberSnapshot> getMembers() {
		return unmodifiableList(memberSnapshots);
	}
	
}
