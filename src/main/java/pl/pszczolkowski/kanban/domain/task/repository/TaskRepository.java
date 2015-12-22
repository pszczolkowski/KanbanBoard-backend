package pl.pszczolkowski.kanban.domain.task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.pszczolkowski.kanban.domain.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query("SELECT Max(t.idOnBoard) FROM Task t WHERE t.boardId = :boardId")
	Optional<Integer> findMaxTaskIdOnBoard(@Param("boardId") long boardId);

	Optional<Task> findById(long taskId);

}
