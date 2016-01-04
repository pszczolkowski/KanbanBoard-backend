package pl.pszczolkowski.kanban.web.restapi.boardhistory;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.pszczolkowski.kanban.domain.board.finder.BoardSnapshotFinder;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.boardhistory.finder.BoardHistorySnapshotFinder;
import pl.pszczolkowski.kanban.domain.boardhistory.snapshot.BoardHistorySnapshot;
import pl.pszczolkowski.kanban.domain.task.finder.ColumnSnapshotFinder;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.service.user.LoggedUserService;

@RestController
@RequestMapping("/board/{boardId}/history")
public class BoardHistoryApi {

	private final BoardSnapshotFinder boardSnapshotFinder;
	private final ColumnSnapshotFinder columnSnapshotFinder;
	private final BoardHistorySnapshotFinder boardHistorySnapshotFinder;

	@Autowired
	public BoardHistoryApi(BoardSnapshotFinder boardSnapshotFinder, ColumnSnapshotFinder columnSnapshotFinder,
			BoardHistorySnapshotFinder boardHistorySnapshotFinder) {
		this.boardSnapshotFinder = boardSnapshotFinder;
		this.columnSnapshotFinder = columnSnapshotFinder;
		this.boardHistorySnapshotFinder = boardHistorySnapshotFinder;
	}

	@ApiOperation(
		value = "Get board history",
		notes = "Returns board history")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Board history returned"),
		@ApiResponse(code = 400, message = "Given input was invalid")})
	@RequestMapping(method = GET)
	public HttpEntity<BoardHistory> get(@PathVariable("boardId") long boardId) {
		BoardSnapshot boardSnapshot = boardSnapshotFinder.findById(boardId);
		if (!loggedUserIsBoardMember(boardSnapshot)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<BoardHistorySnapshot> boardHistorySnapshots = boardHistorySnapshotFinder.findByBoardId(boardId);
		
		List<LocalDateTime> labels = boardHistorySnapshots
			.stream()
			.map(BoardHistorySnapshot::getDate)
			.collect(toList());
		
		List<String> columnNames = columnSnapshotFinder
			.findByBoardId(boardId)
			.stream()
			.map(ColumnSnapshot::getName)
			.collect(toList());
		
		List<List<Integer>> data = new ArrayList<>();
		for (int i = 0; i < columnNames.size(); i++) {
			data.add(new ArrayList<>(boardHistorySnapshots.size()));
		}
		
		for (BoardHistorySnapshot boardHistorySnapshot : boardHistorySnapshots) {
			Map<String, Integer> columnSizes = boardHistorySnapshot.getColumnSizes();
			
			for (int i = 0; i < columnNames.size(); i++) {
				data.get(i).add(columnSizes.getOrDefault(columnNames.get(i), 0));
			}
		}
		
		return ResponseEntity
				.ok()
				.body(new BoardHistory(labels, columnNames, data));
	}
	
	private boolean loggedUserIsBoardMember(BoardSnapshot boardSnapshot) {
		return loggedUserIsBoardMember(LoggedUserService.getSnapshot().getId(), boardSnapshot);
	}
	

	private boolean loggedUserIsBoardMember(Long loggedUserId, BoardSnapshot boardSnapshot) {
		return boardSnapshot
				.getMembers()
				.stream()
				.anyMatch(m -> m.getUserId() == loggedUserId);
	}

}
