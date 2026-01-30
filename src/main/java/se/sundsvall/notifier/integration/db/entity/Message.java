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
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sender_employee_id", nullable = false)
	private Employee sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private UserGroup group;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MessageRecipient> recipients = new HashSet<>();

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}

	public static Message create() {
		return new Message();
	}

	public void addRecipient(MessageRecipient recipient) {
		if (recipient == null)
			return;

		recipients.add(recipient);
		recipient.withMessage(this);

	}

	public void removeRecipient(MessageRecipient recipient) {
		if (recipient == null) {
			return;
		}

		for (var it = recipients.iterator(); it.hasNext();) {
			var r = it.next();

			if (r == recipient) {
				it.remove();
				r.withMessage(null);
				return;
			}
		}
	}

	public Message withTitle(String title) {
		this.title = title;
		return this;
	}

	public Message withContent(String content) {
		this.content = content;
		return this;
	}

	public Message withSender(Employee sender) {
		this.sender = sender;
		return this;
	}

	public Message withGroup(UserGroup group) {
		this.group = group;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Message message))
			return false;
		return id != null && Objects.equals(id, message.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Message{" +
			"id=" + id +
			", title='" + title + '\'' +
			", sender=" + (sender != null ? sender.getId() : "null") +
			", createdAt=" + createdAt +
			'}';
	}
}
