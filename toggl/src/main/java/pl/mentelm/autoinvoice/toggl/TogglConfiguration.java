package pl.mentelm.autoinvoice.toggl;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pl.mentelm.autoinvoice.toggl.openapi.TimeEntriesApi;

@Configuration
@EnableConfigurationProperties(TogglProperties.class)
class TogglConfiguration {

    @Bean
    WebClient togglWebClient(TogglProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Authorization", "Bearer ".concat(properties.getApiToken()))
                .build();
    }

    @Bean
    TimeEntriesApi timeEntriesApi(WebClient togglWebClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(togglWebClient))
                .build();
        return factory.createClient(TimeEntriesApi.class);
    }
}
