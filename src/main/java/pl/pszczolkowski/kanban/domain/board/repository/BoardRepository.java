package pl.pszczolkowski.kanban.domain.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.pszczolkowski.kanban.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	@Query("SELECT b FROM Board b JOIN b.members m WHERE m.userId = :memberId")
	List<Board> findByMemberId(@Param("memberId") long memberId);

	@Query("SELECT b FROM Board b JOIN b.members m WHERE b.name = :name AND m.userId = :memberId")
	List<Board> findByNameAndMemberId(@Param("name") String name, @Param("memberId") long memberId);

}
