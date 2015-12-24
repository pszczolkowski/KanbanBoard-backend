package pl.pszczolkowski.kanban.domain.board.snapshot;

import pl.pszczolkowski.kanban.domain.board.entity.Permissions;

public class BoardMemberSnapshot {
	
	private final long id;
	private final long userId;
	private final Permissions permissions;
	private final long boardId;
	
	public BoardMemberSnapshot(long id, long userId, Permissions permissions, long boardId) {
		this.id = id;
		this.userId = userId;
		this.permissions = permissions;
		this.boardId = boardId;
	}

	public long getId() {
		return id;
	}

	public long getUserId() {
		return userId;
	}

	public Permissions getPermissions() {
		return permissions;
	}

	public long getBoardId() {
		return boardId;
	}
}
