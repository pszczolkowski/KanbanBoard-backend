package pl.pszczolkowski.kanban.domain.label.event;

import org.springframework.context.ApplicationEvent;

import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;

public class LabelDeletedEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6285721266218027334L;

	public LabelDeletedEvent(LabelSnapshot labelSnapshot) {
		super(labelSnapshot);
	}
	
	@Override
	public LabelSnapshot getSource() {
		return (LabelSnapshot) super.getSource();
	}

}
