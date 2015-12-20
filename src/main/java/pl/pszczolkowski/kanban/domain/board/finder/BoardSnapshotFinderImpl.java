package pl.pszczolkowski.kanban.domain.board.finder;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.board.entity.Board;
import pl.pszczolkowski.kanban.domain.board.repository.BoardRepository;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.SnapshotFinder;

@SnapshotFinder
public class BoardSnapshotFinderImpl implements BoardSnapshotFinder {

	private final BoardRepository boardRepository;
	
	@Autowired
	public BoardSnapshotFinderImpl(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	private List<BoardSnapshot> snapshotsFrom(List<Board> boards) {
		return boards.stream()
				.map(Board::toSnapshot)
				.collect(toList());
	}
	
	@Override
	public List<BoardSnapshot> findByOwnerId(long ownerId) {
		List<Board> boards = boardRepository.findByOwnerId(ownerId);
		return snapshotsFrom(boards);
	}

	@Override
	public BoardSnapshot findById(long boardId) {
		Board board = boardRepository.findOne(boardId);
		return board == null ? null : board.toSnapshot();
	}

	@Override
	public BoardSnapshot findByIdAndOwnerId(long boardId, Long ownerId) {
		Board board = boardRepository.findByIdAndOwnerId(boardId, ownerId);
		return board == null ? null : board.toSnapshot();
	}

	@Override
	public List<BoardSnapshot> findByNameAndOWnerId(String name, Long ownerId) {
		List<Board> boards = boardRepository.findByNameAndOwnerId(name, ownerId);
		return snapshotsFrom(boards);
	}

}
