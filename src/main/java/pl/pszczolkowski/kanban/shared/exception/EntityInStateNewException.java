package pl.pszczolkowski.kanban.shared.exception;

public class EntityInStateNewException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityInStateNewException() {
		super("Entity is not persisted yet and cannot be used");
	}

}
