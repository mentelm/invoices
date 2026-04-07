package pl.mentelm.autoinvoice.fakturownia;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "fakturownia")
class FakturowniaProperties {

    /**
     * API token for authentication.
     */
    private String apiToken;

    /**
     * Base URL for Fakturownia API (e.g., https://youraccount.fakturownia.pl).
     */
    private String baseUrl;
}
