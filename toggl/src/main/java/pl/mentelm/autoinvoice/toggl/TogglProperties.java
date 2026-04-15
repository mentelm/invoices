package pl.mentelm.autoinvoice.toggl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "toggl")
class TogglProperties {

    private String apiToken;
    private String baseUrl;
    private Integer organizationId;
    private Integer workspaceId;
    private String defaultJiraProjectCode;
}
