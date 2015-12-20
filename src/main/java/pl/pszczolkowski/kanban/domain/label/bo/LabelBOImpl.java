package pl.pszczolkowski.kanban.domain.label.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.pszczolkowski.kanban.domain.label.entity.Label;
import pl.pszczolkowski.kanban.domain.label.repository.LabelRepository;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.shared.annotations.BusinessObject;

@BusinessObject
public class LabelBOImpl implements LabelBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(LabelBOImpl.class);
	
	private final LabelRepository labelRepository;
	
	@Autowired
	public LabelBOImpl(LabelRepository labelRepository) {
		this.labelRepository = labelRepository;
	}

	@Override
	public LabelSnapshot create(long boardId, String name, String color) {
		Label label = new Label(boardId, name, color);
		label = labelRepository.save(label);
		
		LOGGER.info("Label <{}> created on board <{}>", name, boardId);
		
		return label.toSnapshot();
	}

}
