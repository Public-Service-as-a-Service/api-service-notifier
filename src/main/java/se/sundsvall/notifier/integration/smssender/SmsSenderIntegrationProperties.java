package se.sundsvall.notifier.integration.smssender;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import se.sundsvall.notifier.integration.AbstractRestIntegrationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "integration.sms-sender")
class SmsSenderIntegrationProperties extends AbstractRestIntegrationProperties {
}
