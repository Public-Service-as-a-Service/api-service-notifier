
package se.sundsvall.notifier.integration.smsSender;

import static java.util.Optional.ofNullable;
import static se.sundsvall.notifier.integration.smsSender.MessageStatus.NOT_SENT;
import static se.sundsvall.notifier.integration.smsSender.MessageStatus.SENT;

import generated.se.sundsvall.smssender.SendSmsResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(SmsSenderIntegration.class)
public class SmsSenderIntegration {

	static final String INTEGRATION_NAME = "SmsSender";

	private final SmsSenderClient client;
	private final SmsSenderIntegrationMapper mapper;

	public SmsSenderIntegration(final SmsSenderClient client, SmsSenderIntegrationMapper mapper) {
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
