package se.sundsvall.notifier.integration.teamsSender;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
/*
@FeignClient(
		name = ?,
		url = "${integration.teamsender.base-url}",
		configuration = ?)
@CircuitBreaker(name = ?)
public interface TeamsSenderClient {
	@PostMapping("/{municipalityId}/teams/messages")
	ResponseEntity<SendTeamsResponse>sendTeams(@PathVariable String municipalityId, SendTeamsRequest request);
}
*/