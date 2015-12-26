package pl.pszczolkowski.kanban.domain.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.pszczolkowski.kanban.domain.board.entity.BoardMember;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

	Optional<BoardMember> findByBoardIdAndUserId(long boardId, long userId);

}
