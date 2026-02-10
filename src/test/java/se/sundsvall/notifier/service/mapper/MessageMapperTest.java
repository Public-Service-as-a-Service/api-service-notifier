package se.sundsvall.notifier.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.integration.db.entity.Message;

class MessageMapperTest {
	private MessageMapper messageMapper;

	@BeforeEach
	void setUp() {
		messageMapper = new MessageMapper();
	}

	@Test
	void toEntityTest() {

		var messageRequest = new MessageRequest("title",
			"content",
			"sender",
			null,
			Set.of(1L, 2L, 3L),
			true,
			true);
		var result = messageMapper.toEntity(messageRequest);

		assertThat(result).isNotNull();
		assertThat(result.getTitle()).isEqualTo("title");
		assertThat(result.getSender()).isEqualTo("sender");
		assertThat(result.getContent()).isEqualTo("content");
		assertThat(result.getRecipients()).isNotNull();
	}

	@Test
	void toDtoTest() {
		var createdAt = LocalDateTime.now();
		var message = Message.builder()
			.withId(1L)
			.withTitle("title")
			.withContent("content")
			.withSender("sender")
			.withCreatedAt(createdAt)
			.build();

		var result = messageMapper.entityToMessageResponse(message);

		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(1L);
		assertThat(result.title()).isEqualTo("title");
		assertThat(result.content()).isEqualTo("content");
		assertThat(result.sender()).isEqualTo("sender");
		assertThat(result.createdAt()).isEqualTo(createdAt);
	}
}
