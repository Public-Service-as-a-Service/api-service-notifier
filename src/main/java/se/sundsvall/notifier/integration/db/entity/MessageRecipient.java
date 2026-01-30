package se.sundsvall.notifier.integration.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "message_recipient")
public class MessageRecipient {

	@EmbeddedId
	private MessageRecipientId id = new MessageRecipientId();

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("messageId")
	@JoinColumn(name = "message_id", nullable = false)
	private Message message;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("employeeId")
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	@Column(name = "org_id", nullable = false)
	private String orgId;

	@Column(name = "work_title", length = 100)
	private String workTitle;

	@Column(name = "received_at", nullable = false)
	private LocalDateTime receivedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_status", nullable = false, length = 10)
	private DeliveryStatus deliveryStatus = DeliveryStatus.DELIVERED;

	public enum DeliveryStatus {
		DELIVERED, FAILED
	}

	@PrePersist
	protected void onCreate() {
		if (receivedAt == null) {
			receivedAt = LocalDateTime.now();
		}
		if (orgId == null && employee != null && employee.getOrgId() != null) {
			this.orgId = employee.getOrgId();
		}
		if (workTitle == null && employee != null) {
			this.workTitle = employee.getWorkTitle();
		}
	}

	public static MessageRecipient create() {
		return new MessageRecipient();
	}

	public MessageRecipient withMessageAndEmployee(Message message, Employee employee) {
		this.message = message;
		this.employee = employee;
		syncId();
		return this;
	}

	public MessageRecipient withMessage(Message message) {
		this.message = message;

		if (message == null) {
			id.setMessageId(null);
			return this;
		}

		syncId();
		return this;
	}

	public MessageRecipient withEmployee(Employee employee) {
		this.employee = employee;

		if (employee == null) {
			id.setEmployeeId(null);
			return this;
		}

		syncId();
		return this;
	}

	private void syncId() {
		if (message != null && message.getId() != null) {
			id.setMessageId(message.getId());
		}
		if (employee != null && employee.getId() != null) {
			id.setEmployeeId(employee.getId());
		}
	}

	public MessageRecipient withOrgId(String orgId) {
		this.orgId = orgId;
		return this;
	}

	public MessageRecipient withWorkTitle(String workTitle) {
		this.workTitle = workTitle;
		return this;
	}

	public MessageRecipient withReceivedAt(LocalDateTime receivedAt) {
		this.receivedAt = receivedAt;
		return this;
	}

	public MessageRecipient withDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MessageRecipient that))
			return false;
		return id != null
			&& id.getMessageId() != null
			&& id.getEmployeeId() != null
			&& id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			id != null ? id.getMessageId() : null,
			id != null ? id.getEmployeeId() : null);
	}

	@Override
	public String toString() {
		return "MessageRecipient{" +
			"id=" + id +
			", orgId=" + orgId +
			", workTitle='" + workTitle + '\'' +
			", receivedAt=" + receivedAt +
			", deliveryStatus=" + deliveryStatus +
			'}';
	}
}
