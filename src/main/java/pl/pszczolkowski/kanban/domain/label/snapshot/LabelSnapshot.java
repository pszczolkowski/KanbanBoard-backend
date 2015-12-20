package pl.pszczolkowski.kanban.domain.label.snapshot;

public class LabelSnapshot {

	private final Long id;
	private final long boardId;
	private final String name;
	private final String color;
	
	public LabelSnapshot(Long id, long boardId, String name, String color) {
		super();
		this.id = id;
		this.boardId = boardId;
		this.name = name;
		this.color = color;
	}

	public Long getId() {
		return id;
	}

	public long getBoardId() {
		return boardId;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

}
