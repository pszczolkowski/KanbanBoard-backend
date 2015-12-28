package pl.pszczolkowski.kanban.domain.label.bo;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import pl.pszczolkowski.kanban.domain.label.entity.Label;
import pl.pszczolkowski.kanban.domain.label.event.LabelDeletedEvent;
import pl.pszczolkowski.kanban.domain.label.repository.LabelRepository;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.BusinessObject;

@BusinessObject
public class LabelBOImpl implements LabelBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(LabelBOImpl.class);
	
	private final LabelRepository labelRepository;
	private final ApplicationEventPublisher eventPublisher;
	
	@Autowired
	public LabelBOImpl(LabelRepository labelRepository, ApplicationEventPublisher eventPublisher) {
		this.labelRepository = labelRepository;
		this.eventPublisher = eventPublisher;
	}

	@Override
	public LabelSnapshot create(long boardId, String name, String color) {
		Label label = new Label(boardId, name, color);
		label = labelRepository.save(label);
		
		LOGGER.info("Label <{}> created on board <{}>", name, boardId);
		
		return label.toSnapshot();
	}

	@Override
	public void delete(long labelId) {
		Optional<Label> label = labelRepository.findById(labelId);
		if (label.isPresent()) {
			labelRepository.delete(labelId);
			
			LabelDeletedEvent labelDeletedEvent = new LabelDeletedEvent(label.get().toSnapshot());
			eventPublisher.publishEvent(labelDeletedEvent);
		}
	}

}
