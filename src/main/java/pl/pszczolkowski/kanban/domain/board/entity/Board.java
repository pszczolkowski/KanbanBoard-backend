package pl.pszczolkowski.kanban.domain.board.entity;

import static javax.persistence.GenerationType.AUTO;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	private long ownerId;
	
	@Version
	private long version;
	
	protected Board() {}
	
	public Board(String name, long ownerId) {
		this.name = name;
		this.ownerId = ownerId;
		this.createdAt = LocalDateTime.now();
	}
	
	public BoardSnapshot toSnapshot() {
		if (id == null) {
			throw new EntityInStateNewException();
		}
		
		return new BoardSnapshot(id, name, ownerId, createdAt);
	}

}
