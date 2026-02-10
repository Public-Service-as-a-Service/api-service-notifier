package se.sundsvall.notifier.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.sundsvall.notifier.api.model.request.MessageRequest;
import se.sundsvall.notifier.api.model.response.MessageResponse;
import se.sundsvall.notifier.service.MessageService;

@WebMvcTest(MessageResource.class)
@AutoConfigureMockMvc(addFilters = false)
class MessageResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private MessageService messageService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void createMessageTest() throws Exception {
		var request = MessageRequest.builder()
			.withContent("content")
			.withSender("sender")
			.withTitle("title")
			.withRecipientEmployeeIds(Set.of(1L, 2L, 3L))
			.withSendSms(true)
			.withSendTeams(true)
			.build();

		// Act & Assert
		mockMvc.perform(post("/api/notifier/users/messages")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());

		verify(messageService).createMessage(any(MessageRequest.class));
	}

	@Test
	void testGetMessages() throws Exception {
		var user = "test@sundsvall.se";
		var message = List.of(
			MessageResponse.builder()
				.withId(1L)
				.withTitle("title")
				.withContent("content")
				.build(),
			MessageResponse.builder()
				.withId(2L)
				.withTitle("title2")
				.build());

		when(messageService.getMessages(user)).thenReturn(message);

		mockMvc.perform(get("/api/notifier/users/{user}/messages/", user))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$[0].id").value(1))
			.andExpect(jsonPath("$[0].title").value("title"))
			.andExpect(jsonPath("$[1].id").value(2))
			.andExpect(jsonPath("$[1].title").value("title2"));

		verify(messageService).getMessages(eq(user));
	}

	@Test
	void deleteMessageTest() throws Exception {
		var messageId = 1L;

		mockMvc.perform(delete("/api/notifier/messages/{id}", messageId))
			.andExpect(status().isNoContent());

		verify(messageService).deleteMessages(eq(messageId));
	}

}
