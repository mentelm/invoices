package pl.mentelm.autoinvoice.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.test.StepVerifier;

class JiraIssueCodeExtractorTest {

    private final JiraIssueCodeExtractor extractor = new JiraIssueCodeExtractor("DEFAULT");

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
            "PROJ-123 Task name",
            "[PROJ-123] Task name",
            "PROJ-123",
            "[PROJ-123]",
    })
    void shouldExtractTaskCode(String taskName) {
        StepVerifier.create(extractor.extractTaskCode(taskName))
                .expectNext("PROJ-123")
                .verifyComplete();
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
            "123 Task name",
            "[123] Task name",
            "123",
            "[123]"
    })
    void shouldExtractTaskCodeWithDefaultProjectKey(String taskName) {
        StepVerifier.create(extractor.extractTaskCode(taskName))
                .expectNext("DEFAULT-123")
                .verifyComplete();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("taskNamesResultingInNoJiraCode")
    void shouldReturnNullWhenTaskNameDoesNotStartWithJiraIssue(String taskName) {
        StepVerifier.create(extractor.extractTaskCode(taskName))
                .expectNextCount(0)
                .verifyComplete();
    }

    static String[] taskNamesResultingInNoJiraCode() {
        return new String[]{
                "3A Task name",
                "Just a task",
                "PROJ- Task",
                "-123 Task",
                "[] Task",
                "[PROJ-] Task",
                " ",
                "",
                null
        };
    }
}
