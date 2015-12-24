package pl.pszczolkowski.kanban.web.restapi.task.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;
import static pl.pszczolkowski.kanban.builder.ColumnBuilder.aColumn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;

public class GivenColumnListEndpoint extends GivenStage<GivenColumnListEndpoint> {

	@ProvidedScenarioState
	private List<ColumnSnapshot> columnSnapshots = new ArrayList<>();
	
	private BoardSnapshot boardSnapshot;
	
	public GivenColumnListEndpoint a_request() throws JsonProcessingException {
		prepareRequest(get("/column")
			.param("boardId", String.valueOf(boardSnapshot.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.parseMediaType("application/json;charset=UTF-8")));
		
		return this;
	}

	public GivenColumnListEndpoint a_board() {
		boardSnapshot = aBoard()
			.withAuthorId(getLoggedUser().getId())
			.build();
		
		return this;
	}

	public GivenColumnListEndpoint some_columns() {
		columnSnapshots.add(
			aColumn()
				.withBoardId(boardSnapshot.getId())
				.build());
		columnSnapshots.add(
			aColumn()
				.withBoardId(boardSnapshot.getId())
				.build());
		
		aColumn().build();
		
		return this;
	}

	public GivenColumnListEndpoint a_board_that_logged_user_has_no_access_to() {
		boardSnapshot = aBoard().build();
		return this;
	}

}
