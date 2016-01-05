package pl.pszczolkowski.kanban.web.restapi.column;

public enum WorkInProgressLimitType {

	QUANTITY, SIZE;

	public static WorkInProgressLimitType from(
			pl.pszczolkowski.kanban.domain.task.entity.WorkInProgressLimitType workInProgressLimitType) {
		switch (workInProgressLimitType) {
			case QUANTITY:
				return QUANTITY;
			case SIZE:
				return SIZE;
			default:
				throw new IllegalArgumentException("Unsupported limit type <" + workInProgressLimitType + ">");
		}
	}
	
	public pl.pszczolkowski.kanban.domain.task.entity.WorkInProgressLimitType toDomain() {
		switch (this) {
			case QUANTITY:
				return pl.pszczolkowski.kanban.domain.task.entity.WorkInProgressLimitType.QUANTITY;
			case SIZE:
				return pl.pszczolkowski.kanban.domain.task.entity.WorkInProgressLimitType.SIZE;
			default:
				throw new IllegalArgumentException("Unsupported limit type <" + this + ">");
		}
	}

}
