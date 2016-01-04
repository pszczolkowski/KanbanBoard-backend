package pl.pszczolkowski.kanban.domain.boardhistory.finder;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.boardhistory.entity.BoardHistory;
import pl.pszczolkowski.kanban.domain.boardhistory.repository.BoardHistoryRepository;
import pl.pszczolkowski.kanban.domain.boardhistory.snapshot.BoardHistorySnapshot;
import pl.pszczolkowski.kanban.shared.annotations.SnapshotFinder;

@SnapshotFinder
public class BoardHistorySnapshotFinderImpl implements BoardHistorySnapshotFinder {

	private final BoardHistoryRepository boardHistoryRepository;
	
	@Autowired
	public BoardHistorySnapshotFinderImpl(BoardHistoryRepository boardHistoryRepository) {
		this.boardHistoryRepository = boardHistoryRepository;
	}

	@Override
	public List<BoardHistorySnapshot> findByBoardId(long boardId) {
		List<BoardHistory> boardHistories = boardHistoryRepository.findByBoardIdOrderByDateAsc(boardId);
		return snapshotsOf(boardHistories);
	}

	private List<BoardHistorySnapshot> snapshotsOf(List<BoardHistory> boardHistories) {
		return boardHistories
				.stream()
				.map(BoardHistory::toSnapshot)
				.collect(toList());
	}

}
