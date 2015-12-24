package pl.pszczolkowski.kanban.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.pszczolkowski.kanban.domain.board.entity.BoardMember;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

}
