package pl.pszczolkowski.kanban.domain.board.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import pl.pszczolkowski.kanban.domain.board.entity.Board;
import pl.pszczolkowski.kanban.domain.board.entity.BoardMember;
import pl.pszczolkowski.kanban.domain.board.entity.Permissions;
import pl.pszczolkowski.kanban.domain.board.event.BoardDeletedEvent;
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
	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	public BoardBOImpl(BoardRepository boardRepository, BoardMemberRepository boardMemberRepository,
			ApplicationEventPublisher eventPublisher) {
		this.boardRepository = boardRepository;
		this.boardMemberRepository = boardMemberRepository;
		this.eventPublisher = eventPublisher;
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

	@Override
	public void removeMember(long boardId, long userId) {
		Board board = boardRepository.findOne(boardId);
		
		if (board != null) {
			board.removeMember(userId);
			boardRepository.save(board);
		}
	}

	@Override
	public void setPermissions(Long boardId, Long memberId, Permissions permissions) {
		BoardMember boardMember = boardMemberRepository.findByIdAndBoardId(memberId, boardId);
		
		boardMember.setPermissions(permissions);
		boardMemberRepository.save(boardMember);
	}

	@Override
	public void delete(long boardId) {
		Board board = boardRepository.findOne(boardId);
		if (board != null) {
			boardRepository.delete(board);
			
			BoardDeletedEvent boardDeletedEvent = new BoardDeletedEvent(board.toSnapshot());
			eventPublisher.publishEvent(boardDeletedEvent);
		}
	}

}
