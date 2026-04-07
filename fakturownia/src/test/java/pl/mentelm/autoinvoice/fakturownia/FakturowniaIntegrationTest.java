package pl.mentelm.autoinvoice.fakturownia;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.lifecycle.Startables;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpringBootTest(properties = {
        "spring.main.banner-mode=off"
})
public abstract class FakturowniaIntegrationTest {

    protected static final GenericContainer<?> wiremock = new GenericContainer<>("wiremock/wiremock:3.6.0")
            .withExposedPorts(8080)
            .withEnv(Map.of(
                    "WIREMOCK_OPTIONS", "--verbose --disable-banner --global-response-templating"
            ))
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("wiremock")))
            .withFileSystemBind("src/test/resources/payloads", "/home/wiremock/__files", BindMode.READ_ONLY);

    static {
        Startables.deepStart(wiremock).join();
    }

    protected WireMock wm;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("fakturownia.base-url", () -> "http://" + wiremock.getHost() + ":" + wiremock.getMappedPort(8080));
        registry.add("fakturownia.api-token", () -> "test-token");
    }

    @BeforeEach
    void setUpWireMock() {
        wm = new WireMock(wiremock.getHost(), wiremock.getMappedPort(8080));
        wm.resetMappings();
    }

    protected String readPayload(String path) {
        try {
            return StreamUtils.copyToString(new ClassPathResource("payloads/" + path).getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String renderPayload(String path, InvoiceRequest request) {
        String template = readPayload(path);
        var invoice = request.getInvoice();
        return template
                .replace("{{number}}", String.valueOf(invoice.getNumber()));
    }

    @TestConfiguration
    @ComponentScan("pl.mentelm.autoinvoice.fakturownia")
    static class ContextConfig {

    }
}
