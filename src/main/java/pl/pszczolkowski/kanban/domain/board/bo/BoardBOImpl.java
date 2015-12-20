package pl.pszczolkowski.kanban.domain.board.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.board.entity.Board;
import pl.pszczolkowski.kanban.domain.board.repository.BoardRepository;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.BusinessObject;

@BusinessObject
public class BoardBOImpl implements BoardBO {

	private final static Logger LOGGER = LoggerFactory.getLogger(BoardBOImpl.class);
	private final BoardRepository boardRepository;

	@Autowired
	public BoardBOImpl(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	@Override
	public BoardSnapshot create(String name, long ownerId) {
		Board board = new Board(name, ownerId);
		board = boardRepository.save(board);
		
		LOGGER.info("Board <{}> for user <{}> created", name, ownerId);
		
		return board.toSnapshot();
	}

}
