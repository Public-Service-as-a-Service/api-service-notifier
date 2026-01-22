package se.sundsvall.notifier.integration.smssender;

import static java.util.Optional.ofNullable;
import static se.sundsvall.notifier.integration.smssender.MessageStatus.NOT_SENT;
import static se.sundsvall.notifier.integration.smssender.MessageStatus.SENT;

import generated.se.sundsvall.smssender.SendSmsResponse;
import org.springframework.stereotype.Component;

@Component
public class SmsSenderIntegration {

	private final SmsSenderClient client;
	private final SmsSenderIntegrationMapper mapper;

	SmsSenderIntegration(final SmsSenderClient client, final SmsSenderIntegrationMapper mapper) {
		this.client = client;
		this.mapper = mapper;
	}

	public MessageStatus sendSms(final String municipalityId, final SmsDto dto) {
		var response = client.sendSms(municipalityId, mapper.toSendSmsRequest(dto));
		var success = response.getStatusCode().is2xxSuccessful() &&
			ofNullable(response.getBody())
				.map(SendSmsResponse::getSent)
				.orElse(false);

		return success ? SENT : NOT_SENT;
	}
}
