package pl.pszczolkowski.kanban.domain.board.entity;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import pl.pszczolkowski.kanban.domain.board.snapshot.BoardMemberSnapshot;
import pl.pszczolkowski.kanban.shared.exception.EntityInStateNewException;

@Entity
@Table(
	uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "board_id"})})
public class BoardMember {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@NotNull
	@Min(1)
	private long userId;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Permissions permissions;
	
	@NotNull
	@ManyToOne(fetch = EAGER)
	private Board board;
	
	@Version
	private long version;
	
	protected BoardMember() {}
	
	protected BoardMember(Board board, long userId, Permissions permissions) {
		this.board = board;
		this.userId = userId;
		this.permissions = permissions;
	}
	
	public BoardMemberSnapshot toSnapshot() {
		if (id == null) {
			throw new EntityInStateNewException();
		}
		
		return new BoardMemberSnapshot(id, userId, permissions, board.getId());
	}

	long getUserId() {
		return userId;
	}
	
}
