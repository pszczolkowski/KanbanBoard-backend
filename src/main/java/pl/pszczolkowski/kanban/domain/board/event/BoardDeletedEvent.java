package pl.pszczolkowski.kanban.domain.board.event;

import org.springframework.context.ApplicationEvent;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;

public class BoardDeletedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 8183217776631523936L;

	public BoardDeletedEvent(BoardSnapshot boardSnapshot) {
		super(boardSnapshot);
	}
	
	@Override
	public BoardSnapshot getSource() {
		return (BoardSnapshot) super.getSource();
	}

}
