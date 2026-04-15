package pl.mentelm.autoinvoice.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class JiraIssueCodeExtractor {

    private static final Pattern JIRA_ISSUE_PATTERN = Pattern.compile("^\\[?([A-Z]+-\\d+|\\d+)]?(?!\\S)");

    private final String defaultKeyWithHyphen;

    @Autowired
    JiraIssueCodeExtractor(@Value("${default-jira-project-key}") String defaultJiraProjectKey) {
        this.defaultKeyWithHyphen = defaultJiraProjectKey.concat("-");
    }

    Mono<String> extractTaskCode(String taskName) {
        if (taskName == null || taskName.isBlank()) {
            return Mono.empty();
        }

        Matcher matcher = JIRA_ISSUE_PATTERN.matcher(taskName);

        if (!matcher.find()) {
            return Mono.empty();
        }

        String issueId = matcher.group(1);

        if (issueId.contains("-")) {
            return Mono.just(issueId);
        } else {
            return Mono.just(defaultKeyWithHyphen.concat(issueId));
        }
    }
}
