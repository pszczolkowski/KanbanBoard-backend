package pl.pszczolkowski.kanban.domain.board.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.board.entity.Board;
import pl.pszczolkowski.kanban.domain.board.entity.BoardMember;
import pl.pszczolkowski.kanban.domain.board.entity.Permissions;
import pl.pszczolkowski.kanban.domain.board.repository.BoardMemberRepository;
import pl.pszczolkowski.kanban.domain.board.repository.BoardRepository;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardMemberSnapshot;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.BusinessObject;

@BusinessObject
public class BoardBOImpl implements BoardBO {

	private final static Logger LOGGER = LoggerFactory.getLogger(BoardBOImpl.class);
	
	private final BoardRepository boardRepository;
	private final BoardMemberRepository boardMemberRepository;

	@Autowired
	public BoardBOImpl(BoardRepository boardRepository, BoardMemberRepository boardMemberRepository) {
		this.boardRepository = boardRepository;
		this.boardMemberRepository = boardMemberRepository;
	}

	@Override
	public BoardSnapshot create(String name, long authorId) {
		Board board = new Board(name, authorId);
		board = boardRepository.save(board);
		
		LOGGER.info("Board <{}> created by user <{}> ", name, authorId);
		
		return board.toSnapshot();
	}

	@Override
	public BoardMemberSnapshot addMember(long boardId, long userId, Permissions permissions) {
		Board board = boardRepository.findOne(boardId);
		BoardMember boardMember = board.addMember(userId, permissions);
		
		boardMember = boardMemberRepository.save(boardMember);
		return boardMember.toSnapshot();
	}

}
