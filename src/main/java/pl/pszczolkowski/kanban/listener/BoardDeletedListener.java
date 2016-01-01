package pl.pszczolkowski.kanban.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.kanban.domain.board.event.BoardDeletedEvent;
import pl.pszczolkowski.kanban.domain.task.bo.ColumnBO;

@Component
public class BoardDeletedListener implements ApplicationListener<BoardDeletedEvent> {

	private final ColumnBO columnBO;
	
	@Autowired
	public BoardDeletedListener(ColumnBO columnBO) {
		this.columnBO = columnBO;
	}

	@Override
	public void onApplicationEvent(BoardDeletedEvent event) {
		columnBO.deleteFromBoard(event.getSource().getId());
	}

}
