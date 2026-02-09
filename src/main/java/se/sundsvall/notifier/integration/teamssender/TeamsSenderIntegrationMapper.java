package se.sundsvall.notifier.integration.teamssender;

import generated.se.sundsvall.smssender.SendSmsRequest;
import org.springframework.stereotype.Component;
import generated.se.sundsvall.teamssender.SendTeamsMessageRequest;

@Component
public class TeamsSenderIntegrationMapper {

    public SendTeamsMessageRequest toSendTeamsMessageRequest(final TeamsSenderDTO dto) {
        if (dto == null) {
            return null;
        }

        return new SendTeamsMessageRequest()
                .recipient(dto.recipient())
                .message(dto.message());

    }
}
