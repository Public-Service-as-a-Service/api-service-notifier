package se.sundsvall.notifier.integration.teamssender;

import static se.sundsvall.notifier.integration.teamssender.TeamsSenderConfiguration.CLIENT_ID;

import generated.se.sundsvall.teamssender.SendTeamsMessageRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
	name = CLIENT_ID,
	url = "${integration.teams-sender.base-url}",
	configuration = TeamsSenderConfiguration.class)

@CircuitBreaker(name = CLIENT_ID)
public interface TeamsSenderClient {
	@PostMapping("/{municipalityId}/teams/messages")
	void sendTeamsMessage(@PathVariable String municipalityId, @RequestBody SendTeamsMessageRequest request);
}
