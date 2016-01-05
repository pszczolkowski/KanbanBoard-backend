package pl.pszczolkowski.kanban.domain.label.bo;

import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;

public interface LabelBO {

	LabelSnapshot create(long boardId, String name, String color);

	void delete(long labelId);

	void edit(Long labelId, String name, String color);
	
}
