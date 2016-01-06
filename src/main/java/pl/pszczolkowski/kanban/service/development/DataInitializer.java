package pl.pszczolkowski.kanban.service.development;

import static pl.pszczolkowski.kanban.shared.constant.Profiles.DEVELOPMENT;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import pl.pszczolkowski.kanban.domain.board.bo.BoardBO;
import pl.pszczolkowski.kanban.domain.board.entity.Permissions;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.domain.boardhistory.bo.BoardHistoryBO;
import pl.pszczolkowski.kanban.domain.label.bo.LabelBO;
import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.domain.task.bo.ColumnBO;
import pl.pszczolkowski.kanban.domain.task.bo.TaskBO;
import pl.pszczolkowski.kanban.domain.task.entity.TaskPriority;
import pl.pszczolkowski.kanban.domain.task.entity.WorkInProgressLimitType;
import pl.pszczolkowski.kanban.domain.task.snapshot.ColumnSnapshot;
import pl.pszczolkowski.kanban.domain.user.bo.UserBO;
import pl.pszczolkowski.kanban.domain.user.finder.UserSnapshotFinder;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

@Service
@Profile(DEVELOPMENT)
@Transactional
public class DataInitializer implements ApplicationContextAware {

	private static final Random RANDOM = new Random();
	private static final List<String> USER_LOGINS = Lists.newArrayList("user", "pszczola", "johny", "json");
	private static final String USER_PASSWORD = "user";
	private static final String BOARD_NAME = "Development board";
	private static final List<String> LABEL_NAMES = Lists.newArrayList("Green", "Blue", "Purple", "Red");
	private static final List<String> LABEL_COLORS = Lists.newArrayList("#34a97b", "#067db7", "#77569b", "#cc1a33");
	private static final List<String> COLUMN_NAMES = Lists.newArrayList("To do", "In progress", "Testing", "Done");
	private static final List<String> TASK_NAMES = Lists.newArrayList(
			"cat", "dog", "parrot", "horse", "fish", "hamster", "turtle", "rabbit", "pig", "cow", "turkey", "chicken",
			"rat", "monkey", "donkey", "doplhin", "bird", "butterfly", "badger", "narwhal", "snake", "elephant");
	private static final int DAYS_OF_HISTORY = 31;
	
	private final UserSnapshotFinder userSnapshotFinder;
	private final UserBO userBO;
	private final LabelBO labelBO;
	private final BoardBO boardBO;
	private final ColumnBO columnBO;
	private final TaskBO taskBO;
	private final BoardHistoryBO boardHistoryBO;
	
	private List<UserSnapshot> userSnapshots = new ArrayList<>();
	private BoardSnapshot boardSnapshot;
	private List<LabelSnapshot> labelSnapshots = new ArrayList<>();
	private List<ColumnSnapshot> columnSnapshots = new ArrayList<>();
	
	@Autowired
	public DataInitializer(UserSnapshotFinder userSnapshotFinder,
			UserBO userBO, LabelBO labelBO, BoardBO boardBO, ColumnBO columnBO, TaskBO taskBO,
			BoardHistoryBO boardHistoryBO) {
		this.userSnapshotFinder = userSnapshotFinder;
		this.userBO = userBO;
		this.labelBO = labelBO;
		this.boardBO = boardBO;
		this.columnBO = columnBO;
		this.taskBO = taskBO;
		this.boardHistoryBO = boardHistoryBO;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (userSnapshotFinder.findByLogin(USER_LOGINS.get(0)) == null) {
			createUsers();
			createBoard();
			createLabels();
			createColumns();
			createTasks();
			createBoardHistory();
		}
	}

	private void createUsers() {
		for (String login : USER_LOGINS) {
			userSnapshots.add(userBO.add(login, USER_PASSWORD, login));
		}
	}

	private void createBoard() {
		boardSnapshot = boardBO.create(BOARD_NAME, userSnapshots.get(0).getId());
		
		for (int i = 1; i < userSnapshots.size(); i++) {
			boardBO.addMember(boardSnapshot.getId(), userSnapshots.get(i).getId(), Permissions.NORMAL);
		}
	}
	
	private void createLabels() {
		for (int i = 0; i < LABEL_NAMES.size(); i++) {
			labelSnapshots.add(labelBO.create(boardSnapshot.getId(), LABEL_NAMES.get(i), LABEL_COLORS.get(i)));
		}
	}
	
	private void createColumns() {
		for (String columnName : COLUMN_NAMES) {
			columnSnapshots.add(columnBO.add(boardSnapshot.getId(), columnName, null, WorkInProgressLimitType.QUANTITY));
		}
	}

	private void createTasks() {
		for (String taskName : TASK_NAMES) {
			taskBO.create(randomColumn(), taskName, null, randomAssignee(), randomLabel(), randomPriority(), randomSize());
		}
	}
	
	private long randomColumn() {
		return columnSnapshots.get(RANDOM.nextInt(columnSnapshots.size())).getId();
	}

	private Long randomAssignee() {
		int random = RANDOM.nextInt(userSnapshots.size() + 1) - 1;
		
		if (random == -1) {
			return null;
		} else {
			return userSnapshots.get(random).getId();
		}
	}

	private Long randomLabel() {
		int random = RANDOM.nextInt(labelSnapshots.size() + 1) - 1;
		
		if (random == -1) {
			return null;
		} else {
			return labelSnapshots.get(random).getId();
		}
	}
	
	private TaskPriority randomPriority() {
		TaskPriority[] taskPriorities = TaskPriority.values();
		return taskPriorities[RANDOM.nextInt(taskPriorities.length)];
	}
	
	private float randomSize() {
		return RANDOM.nextInt(5) + 1;
	}
	
	private void createBoardHistory() {
		LocalDate now = LocalDate.now();
		LocalDate date = now.minusDays(DAYS_OF_HISTORY);
		
		Map<String, Integer> columnSizes = new HashMap<>();
		for (ColumnSnapshot columnSnapshot : columnSnapshots) {
			columnSizes.put(columnSnapshot.getName(), 0);
		}
		columnSizes.put(columnSnapshots.get(0).getName(), 5);
		
		while (date.isBefore(now)) {
			boardHistoryBO.save(boardSnapshot.getId(), date, columnSizes);
			
			for (int i = columnSnapshots.size() - 2; i >= 0; i--) {
				if (columnSizes.get(columnSnapshots.get(i).getName()) > 0) {
					if (RANDOM.nextBoolean()) {
						columnSizes.put(columnSnapshots.get(i).getName(), columnSizes.get(columnSnapshots.get(i).getName()) - 1);
						columnSizes.put(columnSnapshots.get(i + 1).getName(), columnSizes.get(columnSnapshots.get(i + 1).getName()) + 1);
					}
				}
			}
			
			if (RANDOM.nextBoolean() && RANDOM.nextBoolean()) {
				columnSizes.put(columnSnapshots.get(0).getName(), columnSizes.get(columnSnapshots.get(0).getName()) + 1);
			}
			
			date = date.plusDays(1);
		}
	}
	
}
