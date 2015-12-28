package pl.pszczolkowski.kanban.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.kanban.domain.label.event.LabelDeletedEvent;
import pl.pszczolkowski.kanban.domain.task.bo.TaskBO;

@Component
public class LabelDeletedListener implements ApplicationListener<LabelDeletedEvent> {

	private final TaskBO taskBO;
	
	@Autowired
	public LabelDeletedListener(TaskBO taskBO) {
		this.taskBO = taskBO;
	}

	@Override
	public void onApplicationEvent(LabelDeletedEvent event) {
		taskBO.detachLabelFromTasksInBoard(event.getSource().getId());
	}

}
