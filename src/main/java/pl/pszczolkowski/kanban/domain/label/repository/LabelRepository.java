package pl.pszczolkowski.kanban.domain.label.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.pszczolkowski.kanban.domain.label.entity.Label;

public interface LabelRepository extends JpaRepository<Label, Long> {

	List<Label> findByBoardId(long boardId);

	Optional<Label> findByBoardIdAndName(long boardId, String name);

	Optional<Label> findByIdAndBoardId(Long labelId, long boardId);

	Optional<Label> findById(long labelId);

}
