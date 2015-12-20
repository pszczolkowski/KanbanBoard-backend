package pl.pszczolkowski.kanban.domain.label.finder;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.label.entity.Label;
import pl.pszczolkowski.kanban.domain.label.repository.LabelRepository;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.SnapshotFinder;

@SnapshotFinder
public class LabelSnapshotFinderImpl implements LabelSnapshotFinder {

	private final LabelRepository labelRepository;
	
	@Autowired
	public LabelSnapshotFinderImpl(LabelRepository labelRepository) {
		this.labelRepository = labelRepository;
	}

	@Override
	public List<LabelSnapshot> findByBoardId(long boardId) {
		List<Label> labels = labelRepository.findByBoardId(boardId);
		return snapshotsFrom(labels);
	}

	private List<LabelSnapshot> snapshotsFrom(List<Label> labels) {
		return labels
				.stream()
				.map(Label::toSnapshot)
				.collect(toList());
	}

	@Override
	public LabelSnapshot findByBoardIdAndName(long boardId, String name) {
		return labelRepository
				.findByBoardIdAndName(boardId, name)
				.map(Label::toSnapshot)
				.orElse(null);
	}

	@Override
	public LabelSnapshot findByIdAndBoardId(Long labelId, long boardId) {
		return labelRepository
				.findByIdAndBoardId(labelId, boardId)
				.map(Label::toSnapshot)
				.orElse(null);
	}

}
