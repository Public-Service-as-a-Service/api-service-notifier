package se.sundsvall.notifier.integration.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "member")
public class Member {

	@EmbeddedId
	private MemberId id = new MemberId();

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("employeeId")
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("groupId")
	@JoinColumn(name = "group_id", nullable = false)
	private UserGroup group;

	@Column(name = "joined_at", nullable = false)
	private LocalDateTime joinedAt;

	@PrePersist
	protected void onCreate() {
		if (joinedAt == null) {
			joinedAt = LocalDateTime.now();
		}
	}

	public static Member create() {
		return new Member();
	}

	public Member withEmployee(Employee employee) {
		this.employee = employee;

		if (employee == null) {
			id.setEmployeeId(null);
			return this;
		}

		syncId();
		return this;
	}

	public Member withGroup(UserGroup group) {
		this.group = group;

		if (group == null) {
			id.setGroupId(null);
			return this;
		}

		syncId();
		return this;
	}

	public Member withEmployeeAndGroup(Employee employee, UserGroup group) {
		return withEmployee(employee).withGroup(group);
	}

	public Member withJoinedAt(LocalDateTime joinedAt) {
		this.joinedAt = joinedAt;
		return this;
	}

	private void syncId() {
		if (employee != null && employee.getId() != null) {
			id.setEmployeeId(employee.getId());
		}
		if (group != null && group.getId() != null) {
			id.setGroupId(group.getId());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Member member))
			return false;
		return id != null && id.equals(member.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Member{" +
			"id=" + id +
			", joinedAt=" + joinedAt +
			'}';
	}
}
