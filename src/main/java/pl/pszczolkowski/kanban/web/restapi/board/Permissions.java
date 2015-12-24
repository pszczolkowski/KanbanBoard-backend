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
	
}
