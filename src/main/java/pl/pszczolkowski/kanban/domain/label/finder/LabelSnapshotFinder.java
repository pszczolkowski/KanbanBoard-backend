package pl.pszczolkowski.kanban.domain.label.finder;

import java.util.List;

import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;

public interface LabelSnapshotFinder {

	List<LabelSnapshot> findByBoardId(long boardId);
	
	LabelSnapshot findByBoardIdAndName(long boardId, String name);

	LabelSnapshot findByIdAndBoardId(Long labelId, long boardId);

	LabelSnapshot findById(long labelId);
	
}
