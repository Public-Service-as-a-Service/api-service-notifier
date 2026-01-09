package se.sundsvall.notifier.integration;

import static se.sundsvall.notifier.integration.Constants.DEFAULT_CONNECT_TIMEOUT;
import static se.sundsvall.notifier.integration.Constants.DEFAULT_READ_TIMEOUT;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractRestIntegrationProperties {

	private String baseUrl;

	private Duration readTimeout = DEFAULT_READ_TIMEOUT;
	private Duration connectTimeout = DEFAULT_CONNECT_TIMEOUT;

	private String tokenUrl;
	private String clientId;
	private String clientSecret;
	private String grantType = "client_credentials";
}
