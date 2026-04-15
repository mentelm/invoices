package pl.mentelm.autoinvoice.time;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.mentelm.autoinvoice.domain.GetTimeEntries;
import pl.mentelm.autoinvoice.model.time.TimeEntrySummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
class TimeSummaryController {

    private final GetTimeEntries getTimeEntries;
    private final JiraIssueCodeExtractor jiraIssueCodeExtractor;

    @GetMapping(
            path = "/time-summary/{yearMonth}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    Flux<TimeEntrySummary> getTimeSummary(@PathVariable String yearMonth) {
        return getTimeEntries.summarizeEntries(YearMonth.parse(yearMonth))
                .flatMap(this::overideDescriptionWithJiraCode);
    }

    private Mono<TimeEntrySummary> overideDescriptionWithJiraCode(TimeEntrySummary summary) {
        return jiraIssueCodeExtractor.extractTaskCode(summary.description())
                .map(code -> new TimeEntrySummary(code, summary.dailyDurations()));
    }
}
