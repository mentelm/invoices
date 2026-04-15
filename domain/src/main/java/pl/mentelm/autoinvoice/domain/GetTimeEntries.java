package pl.mentelm.autoinvoice.domain;

import pl.mentelm.autoinvoice.model.time.TimeEntrySummary;
import reactor.core.publisher.Flux;

import java.time.YearMonth;

public interface GetTimeEntries {

    Flux<TimeEntrySummary> summarizeEntries(YearMonth yearMonth);
}
