package se.sundsvall.notifier.integration.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MessageRecipientId implements Serializable {

	@Column(name = "message_id")
	private Long messageId;

	@Column(name = "employee_id")
	private Long employeeId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MessageRecipientId that = (MessageRecipientId) o;
		return Objects.equals(messageId, that.messageId) &&
			Objects.equals(employeeId, that.employeeId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId, employeeId);
	}

	@Override
	public String toString() {
		return "MessageRecipientId{" +
			"messageId=" + messageId +
			", employeeId=" + employeeId +
			'}';
	}
}
