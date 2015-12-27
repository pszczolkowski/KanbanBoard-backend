package pl.pszczolkowski.kanban.domain.board.entity;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardMemberSnapshot;
import pl.pszczolkowski.kanban.domain.board.snapshot.BoardSnapshot;
import pl.pszczolkowski.kanban.shared.exception.EntityInStateNewException;

@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 50)
	private String name;
	
	@NotNull
	private LocalDateTime createdAt;
	
	@NotNull
	@OneToMany(cascade = ALL, fetch = EAGER, mappedBy = "board", orphanRemoval = true)
	private final List<BoardMember> members = new ArrayList<>();
	
	@Version
	private long version;
	
	protected Board() {}
	
	public Board(String name, long authorId) {
		this.name = name;
		this.createdAt = LocalDateTime.now();
		
		addMember(authorId, Permissions.ADMIN);
	}
	
	public BoardSnapshot toSnapshot() {
		if (id == null) {
			throw new EntityInStateNewException();
		}
		
		List<BoardMemberSnapshot> memberSnapshots = members
			.stream()
			.map(BoardMember::toSnapshot)
			.collect(toList());
		
		return new BoardSnapshot(id, name, createdAt, memberSnapshots);
	}
	
	public BoardMember addMember(long userId, Permissions permissions) {
		if (indexOfMember(userId).isPresent()) {
			throw new IllegalArgumentException("User with id <" + userId + "> is already board member");
		}
		
		BoardMember boardMember = new BoardMember(this, userId, permissions);
		this.members.add(boardMember);
		
		return boardMember;
	}

	long getId() {
		return id;
	}

	public void removeMember(long userId) {
		Optional<Integer> position = indexOfMember(userId);
		if (position.isPresent()) {
			members.remove(position.get().intValue());
		}
	}

	private Optional<Integer> indexOfMember(long userId) {
		Integer position = null;
		for (int i = 0; i < members.size(); i++) {
			if (members.get(i).getUserId() == userId) {
				position = i;
				break;
			}
		}
		
		return Optional.ofNullable(position);
	}

}
