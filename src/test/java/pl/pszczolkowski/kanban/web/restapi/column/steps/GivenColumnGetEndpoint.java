package pl.pszczolkowski.kanban.web.restapi.column.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;
import static pl.pszczolkowski.kanban.builder.ColumnBuilder.aColumn;

import org.springframework.http.MediaType;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;

public class GivenColumnGetEndpoint extends GivenStage<GivenColumnGetEndpoint> {
	
	@ProvidedScenarioState
	private ColumnSnapshot columnSnapshot;
	
	private BoardSnapshot boardSnapshot;

	public GivenColumnGetEndpoint a_board() {
		boardSnapshot = aBoard()
			.withAuthorId(getLoggedUser().getId())
			.build();
		
		return this;
	}

	public GivenColumnGetEndpoint a_board_that_logged_user_has_no_access_to() {
		boardSnapshot = aBoard().build();
		return this;
	}

	public GivenColumnGetEndpoint a_column() {
		columnSnapshot = aColumn()
			.withBoardId(boardSnapshot.getId())
			.build();
		
		return this;
	}

	public GivenColumnGetEndpoint a_request() {
		prepareRequest(
			get("/column/{id}", columnSnapshot.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_VALUE));
		
		return this;
	}

	public GivenColumnGetEndpoint a_board_that_user_has_no_access_to() {
		boardSnapshot = aBoard().build();
		return this;
	}

	public GivenColumnGetEndpoint a_request_with_not_existing_column_id() {
		long notExistingColumnId = Long.MAX_VALUE;
		
		prepareRequest(
			get("/column/{id}", notExistingColumnId)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_VALUE));
		
		return this;
	}

}
