package pl.mentelm.autoinvoice.fakturownia;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties(FakturowniaProperties.class)
class FakturowniaConfiguration {

    @Bean
    WebClient fakturowniaWebClient(FakturowniaProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    @Bean
    FakturowniaClient fakturowniaClient(WebClient fakturowniaWebClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(fakturowniaWebClient))
                .build();
        return factory.createClient(FakturowniaClient.class);
    }
}
