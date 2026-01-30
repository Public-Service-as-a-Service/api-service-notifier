package se.sundsvall.notifier.integration.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_group")
public class UserGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_id")
	private Long id;

	@Column(name = "group_name", nullable = false)
	private String name;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "creator_employee_id", nullable = false)
	private Employee creator;

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Member> members = new HashSet<>();

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
		if (updatedAt == null) {
			updatedAt = LocalDateTime.now();
		}
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public static UserGroup create() {
		return new UserGroup();
	}

	public void addEmployee(Employee employee) {
		if (employee == null)
			return;

		boolean alreadyMember = members.stream()
			.anyMatch(m -> m.getEmployee() != null && m.getEmployee().equals(employee));

		if (alreadyMember)
			return;

		Member member = Member.create()
			.withEmployee(employee)
			.withGroup(this);

		members.add(member);

		employee.addMembership(member);
	}

	public boolean removeEmployee(Employee employee) {
		if (employee == null)
			return false;

		for (var it = members.iterator(); it.hasNext();) {
			Member m = it.next();
			if (employee.equals(m.getEmployee())) {
				it.remove();
				employee.getMemberships().remove(m);

				m.setGroup(null);
				m.setEmployee(null);

				return true;
			}
		}
		return false;
	}

	public UserGroup withName(String name) {
		this.name = name;
		return this;
	}

	public UserGroup withDescription(String description) {
		this.description = description;
		return this;
	}

	public UserGroup withCreator(Employee creator) {
		this.creator = creator;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UserGroup userGroup))
			return false;
		return id != null && Objects.equals(id, userGroup.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "UserGroup{" +
			"id=" + id +
			", name='" + name + '\'' +
			", creator=" + (creator != null ? creator.getId() : "null") +
			", createdAt=" + createdAt +
			'}';
	}
}
