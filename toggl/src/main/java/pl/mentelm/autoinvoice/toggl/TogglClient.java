package pl.mentelm.autoinvoice.toggl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.mentelm.autoinvoice.domain.GetTimeEntries;
import pl.mentelm.autoinvoice.model.time.TimeEntrySummary;
import pl.mentelm.autoinvoice.toggl.openapi.TimeEntriesApi;
import pl.mentelm.autoinvoice.toggl.openapi.TimeentryTimeEntryWithTask;
import reactor.core.publisher.Flux;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class TogglClient implements GetTimeEntries {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_INSTANT;
    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Warsaw");

    private final TimeEntriesApi timeEntriesApi;
    private final TogglProperties properties;

    @Override
    public Flux<TimeEntrySummary> summarizeEntries(YearMonth yearMonth) {
        return timeEntriesApi.listStreamTimeEntriesRangeWithOrg(
                        properties.getOrganizationId(),
                        properties.getWorkspaceId(),
                        formatted(yearMonth.atDay(1), LocalTime.MIN),
                        formatted(yearMonth.atEndOfMonth(), LocalTime.MAX),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                )
//                .log()
                .collectMultimap(
                        TogglClient::extractTaskName,
                        Function.identity()
                )
                .flatMapMany(groupedEntries -> Flux.fromStream(
                        groupedEntries.entrySet().stream()
                                .map(entry -> new TimeEntrySummary(
                                        entry.getKey(),
                                        collectDailyDurations(entry.getValue())
                                ))
                ));
    }

    private static String extractTaskName(TimeentryTimeEntryWithTask entry) {
        return entry.getTask() != null ? entry.getTask().getDescription() : "No Task";
    }

    private static LocalDate toLocalDate(String createdDate) {
        return Instant.parse(createdDate)
                .atZone(ZONE_ID)
                .toLocalDate();
    }

    private static Map<LocalDate, Duration> collectDailyDurations(Collection<TimeentryTimeEntryWithTask> entries) {
        return entries.stream()
                .collect(Collectors.toMap(
                        entry -> toLocalDate(entry.getCreatedAt()),
                        entry -> Duration.ofSeconds(entry.getDuration()),
                        Duration::plus
                ));
    }

    private static String formatted(LocalDate localDate, LocalTime localTime) {
        return DATE_FORMATTER.format(Instant.from(
                ZonedDateTime.of(localDate, localTime, ZONE_ID)
        ));
    }
}
