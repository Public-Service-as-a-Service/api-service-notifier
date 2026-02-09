package se.sundsvall.notifier.integration.teamssender;

import org.springframework.stereotype.Component;

@Component
public class TeamsSenderIntegration {

    private final TeamsSenderClient teamsSenderClient;
    private final TeamsSenderIntegrationMapper mapper;

    public TeamsSenderIntegration(TeamsSenderClient teamsSenderClient, TeamsSenderIntegrationMapper mapper) {
        this.teamsSenderClient = teamsSenderClient;
        this.mapper = mapper;
    }

    public void sendTeamsMessage(final String municipalityId, final TeamsSenderDTO dto) {
        teamsSenderClient.sendTeamsMessage(municipalityId, mapper.toSendTeamsMessageRequest(dto));
    }
}
