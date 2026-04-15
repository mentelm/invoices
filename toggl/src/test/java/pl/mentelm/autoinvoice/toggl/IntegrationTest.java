package pl.mentelm.autoinvoice.toggl;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.mentelm.autoinvoice.toggl.openapi.TimeEntriesApi;

@SpringBootTest(
        properties = {
                "spring.main.banner-mode=off",
                "toggl.defaultJiraProjectCode=MNTLM",
                "toggl.baseUrl=https://focus.toggl.com/api",
                "toggl.api-token=toggl_sk_96e65447e82b6c2f9d1f8aa535bc18e7",
                "toggl.organization-id=6414950",
                "toggl.workspace-id=6442663",
        }
)
@ContextConfiguration(classes = IntegrationTest.ContextConfig.class)
public abstract class IntegrationTest {

    @MockitoBean
    protected TimeEntriesApi timeEntriesApi;

    @ComponentScan("pl.mentelm.autoinvoice.toggl")
    static class ContextConfig {

    }
}
