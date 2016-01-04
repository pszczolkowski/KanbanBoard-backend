package pl.pszczolkowski.kanban.builder;

import java.util.Random;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.kanban.domain.task.entity.Column;
import pl.pszczolkowski.kanban.domain.task.repository.ColumnRepository;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;

@Component
public class ColumnBuilder implements ApplicationContextAware {

	private static final Random RANDOM = new Random();
	
	private static ColumnRepository columnRepository;
	
	private String name = "Column" + RANDOM.nextInt();
	private long boardId = Long.MAX_VALUE;
	private int position = 0;
	private Integer workInProgressLimit = null;
	
	public ColumnBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public ColumnBuilder withBoardId(long boardId) {
		this.boardId = boardId;
		return this;
	}

	public ColumnBuilder withPosition(int position) {
		this.position = position;
		return this;
	}

	public ColumnBuilder withWorkInProgressLimit(Integer workInProgressLimit) {
		this.workInProgressLimit = workInProgressLimit;
		return this;
	}

	public ColumnSnapshot build() {
		if (columnRepository == null) {
			throw new IllegalStateException("Required ColumnRepository dependency has not been initialized yet");
		}
		
		Column column = new Column(boardId, name, position, workInProgressLimit);
		column = columnRepository.save(column);
		
		return column.toSnapshot();
	}
	
	public static ColumnBuilder aColumn() {
		return new ColumnBuilder();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		columnRepository = applicationContext.getBean(ColumnRepository.class);		
	}

}
