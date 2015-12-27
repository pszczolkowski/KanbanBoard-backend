package pl.pszczolkowski.kanban.web.restapi.board;

public enum Permissions {

	ADMIN, NORMAL;

	public static Permissions from(pl.pszczolkowski.kanban.domain.board.entity.Permissions permissions) {
		switch (permissions) {
			case ADMIN:
				return Permissions.ADMIN;
			case NORMAL:
				return Permissions.NORMAL;
			default:
				throw new IllegalArgumentException("Unsupported permission type <" + permissions + ">");
		}
	}

	public pl.pszczolkowski.kanban.domain.board.entity.Permissions toDomain() {
		switch (this) {
			case ADMIN:
				return pl.pszczolkowski.kanban.domain.board.entity.Permissions.ADMIN;
			case NORMAL:
				return pl.pszczolkowski.kanban.domain.board.entity.Permissions.NORMAL;
			default:
				throw new IllegalArgumentException("Unsupported permission type <" + this + ">");
		}
	}
	
}
