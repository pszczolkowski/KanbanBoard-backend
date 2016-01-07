package pl.pszczolkowski.kanban.builder;

import java.util.Random;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.kanban.domain.label.entity.Label;
import pl.pszczolkowski.kanban.domain.label.repository.LabelRepository;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;

@Component
public class LabelBuilder implements ApplicationContextAware {

	private static final String CLAZZ = LabelBuilder.class.getSimpleName().substring(0, 10);
	private static final Random RANDOM = new Random();

	private static LabelRepository labelRepository;
	
	private long boardId = Integer.MAX_VALUE;
	private String name = CLAZZ + RANDOM.nextInt();
	private String color = ranomColor();
	
	public LabelBuilder withBoardId(long boardId) {
		this.boardId = boardId;
		return this;
	}
	
	private String ranomColor() {
		String color = "#";
		for (int i = 0; i < 6; i++) {
			color += RANDOM.nextInt(10);
		}
		
		return color;
	}

	public LabelBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public LabelBuilder withColor(String color) {
		this.color = color;
		return this;
	}
	
	public LabelSnapshot build() {
		if (labelRepository == null) {
			throw new IllegalStateException("Required LabelRepository dependency has not been initialized yet");
		}
		
		Label label = new Label(boardId, name, color);
		label = labelRepository.save(label);
		
		return label.toSnapshot();
	}
	
	public static LabelBuilder aLabel() {
		return new LabelBuilder();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		labelRepository = applicationContext.getBean(LabelRepository.class);
	}
	
}
