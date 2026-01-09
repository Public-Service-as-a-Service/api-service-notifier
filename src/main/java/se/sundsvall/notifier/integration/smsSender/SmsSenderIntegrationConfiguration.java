package se.sundsvall.notifier.integration.smsSender;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
/*
@Import(FeignConfiguration.class)
@EnableConfigurationProperties
public class SmsSenderIntegrationConfiguration {
    public static final String INTEGRATION_NAME = "smssender";

    @Bean
    FeignBuilderCustomizer feignBuilderCustomizer(SmsSenderIntegrationProperties smsSenderIntegrationProperties) {
        return FeignMultiCustomizer.create()
                .withErrorDecoder(new ProblemErrorDecoder(CLIENT_ID))
                .withRequestTimeoutsInSeconds(citizenIntegrationProperties.connectTimeout(), citizenIntegrationProperties.readTimeout())
                .composeCustomizersToOne();
    }

}*/
