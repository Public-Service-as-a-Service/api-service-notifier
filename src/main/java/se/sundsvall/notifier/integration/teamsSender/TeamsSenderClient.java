/*
 * package se.sundsvall.notifier.integration.teamsSender;
 * 
 * 
 * @FeignClient(
 * name = ?,
 * url = "${integration.teamsender.base-url}",
 * configuration = ?)
 * 
 * @CircuitBreaker(name = ?)
 * public interface TeamsSenderClient {
 * 
 * @PostMapping("/{municipalityId}/teams/messages")
 * ResponseEntity<SendTeamsResponse>sendTeams(@PathVariable String municipalityId, SendTeamsRequest request);
 * }
 */
