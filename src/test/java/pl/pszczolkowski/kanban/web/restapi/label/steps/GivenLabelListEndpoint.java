package pl.pszczolkowski.kanban.web.restapi.label.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static pl.pszczolkowski.kanban.builder.BoardBuilder.aBoard;
import static pl.pszczolkowski.kanban.builder.LabelBuilder.aLabel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.web.restapi.shared.GivenStage;

public class GivenLabelListEndpoint extends GivenStage<GivenLabelListEndpoint> {
	
	private static final int LABELS_NUMBER = 4;

	@ProvidedScenarioState
	private final List<LabelSnapshot> labelSnapshots = new ArrayList<>();
	
	private BoardSnapshot boardSnapshot;

	public GivenLabelListEndpoint a_board() {
		boardSnapshot = aBoard()
			.withAuthorId(getLoggedUser().getId())
			.build();
		
		return this;
	}
	
	public GivenLabelListEndpoint a_board_that_user_has_no_access_to() {
		boardSnapshot = aBoard().build();
		return this;
	}

	public GivenLabelListEndpoint a_request() {
		prepareRequest(
			get("/label")
			.param("boardId", String.valueOf(boardSnapshot.getId()))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON_VALUE));
		
		return this;
	}

	public GivenLabelListEndpoint some_labels() {
		for (int i = 0; i < LABELS_NUMBER; i++) {
			labelSnapshots.add(
				aLabel()
					.withBoardId(boardSnapshot.getId())
					.build());
		}
		
		return this;
	}

}
