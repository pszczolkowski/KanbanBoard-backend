package pl.pszczolkowski.kanban.domain.board.bo;

import pl.pszczolkowski.kanban.domain.board.entity.Permissions;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardMemberSnapshot;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;

public interface BoardBO {

	BoardSnapshot create(String name, long authorId);
	
	BoardMemberSnapshot addMember(long boardId, long userId, Permissions permissions);

	void removeMember(long boardId, long userId);

	void setPermissions(Long boardId, Long memberId, Permissions permissions);
	
}
