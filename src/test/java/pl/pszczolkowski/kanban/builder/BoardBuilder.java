package pl.pszczolkowski.kanban.builder;

import java.util.Random;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.kanban.domain.board.entity.Board;
import pl.pszczolkowski.kanban.domain.board.repository.BoardRepository;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;

@Component
public class BoardBuilder implements ApplicationContextAware {

	private static final String CLAZZ = BoardBuilder.class.getSimpleName();
	private static final Random RANDOM = new Random();

	
	private static BoardRepository boardRepository;
	
	private String name = CLAZZ + RANDOM.nextInt();
	private long ownerId = Long.MAX_VALUE;
	
	public BoardBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public BoardBuilder withOwnerId(long ownerId) {
		this.ownerId = ownerId;
		return this;
	}
	
	public BoardSnapshot build() {
		if (boardRepository == null) {
			throw new IllegalStateException("Required BoardRepository dependency has not been initialized yet");
		}
		
		Board board = new Board(name, ownerId);
		board = boardRepository.save(board);
		
		return board.toSnapshot();
	}
	
	public static BoardBuilder aBoard() {
		return new BoardBuilder();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		boardRepository = applicationContext.getBean(BoardRepository.class);
	}
	
}
