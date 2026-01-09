package se.sundsvall.notifier.integration.smsSender;

import generated.se.sundsvall.smssender.SendSmsRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
/*
@FeignClient(
	name = INTEGRATION_NAME,
	url = "${integration.sms-sender.base-url}",
	configuration = SmsSenderIntegrationConfiguration.class)
@CircuitBreaker(name = INTEGRATION_NAME)
interface SmsSenderClient {
	@PostMapping("/{municipalityId}/send/sms")
	ResponseEntity<SendSmsRequest> sendSms(@PathVariable String municipalityId, SendSmsRequest request);
}
 */