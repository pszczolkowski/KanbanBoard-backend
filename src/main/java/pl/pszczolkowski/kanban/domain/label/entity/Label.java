package pl.pszczolkowski.kanban.domain.label.entity;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import pl.pszczolkowski.kanban.domain.label.snapshot.LabelSnapshot;
import pl.pszczolkowski.kanban.shared.exception.EntityInStateNewException;

@Entity
@Table(
	uniqueConstraints = @UniqueConstraint(columnNames = {"boardId", "name"}))
public class Label {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;
	
	@NotNull
	@Min(1)
	private long boardId;
	
	@NotEmpty
	@Size(max = 100)
	private String name;
	
	@NotNull
	@Size(min = 7, max = 7)
	@Pattern(regexp = "^#[a-fA-F0-9]{6}$")
	private String color;
	
	@Version
	private long version;
	
	protected Label() {}

	public Label(long boardId, String name, String color) {
		this.boardId = boardId;
		this.name = name;
		this.color = color;
	}
	
	public LabelSnapshot toSnapshot() {
		if (id == null) {
			throw new EntityInStateNewException();
		}
		
		return new LabelSnapshot(id, boardId, name, color);
	}
	
}
