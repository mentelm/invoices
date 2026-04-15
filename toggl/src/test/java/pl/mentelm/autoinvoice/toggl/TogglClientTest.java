package pl.mentelm.autoinvoice.toggl;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.mentelm.autoinvoice.toggl.openapi.ModelsTask;
import pl.mentelm.autoinvoice.toggl.openapi.TimeentryTimeEntryWithTask;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;

class TogglClientTest extends IntegrationTest {

    @Autowired
    TogglClient togglClient;

    @AfterEach
    void resetMocks() {
        reset(timeEntriesApi);
    }

    @Test
    void correctlyGroupsEntriesByDate() {
        TimeentryTimeEntryWithTask entry1 = getEntry("PROJECT-123 Task 1", "2026-03-01T10:00:00Z", 3600);
        TimeentryTimeEntryWithTask entry2 = getEntry("PROJECT-123 Task 1", "2026-03-01T12:00:00Z", 3600);
        TimeentryTimeEntryWithTask entry3 = getEntry("PROJECT-123 Task 1", "2026-03-02T08:00:00Z", 7200);

        given(timeEntriesApi.listStreamTimeEntriesRangeWithOrg(
                any(), any(), any(), any(), eq(Optional.empty()), eq(Optional.empty()), eq(Optional.empty()), eq(Optional.empty()), eq(Optional.empty())
        )).willReturn(Flux.just(entry1, entry2, entry3));

        StepVerifier.create(togglClient.summarizeEntries(YearMonth.of(2026, Month.MARCH)))
                .assertNext(summary -> {
                    assertThat(summary.description()).isEqualTo("PROJECT-123 Task 1");

                    assertThat(summary.dailyDurations())
                            .containsExactlyInAnyOrderEntriesOf(Map.of(
                                    LocalDate.of(2026, 3, 1), Duration.ofHours(2),
                                    LocalDate.of(2026, 3, 2), Duration.ofHours(2)
                            ));
                })
                .verifyComplete();
    }

    @Test
    void correctlyGroupsEntriesByCodeAndDescription() {
        TimeentryTimeEntryWithTask entry1 = getEntry("PROJECT-123 Task 1", "2026-03-01T10:00:00Z", 3600);
        TimeentryTimeEntryWithTask entry2 = getEntry("PROJECT-124 Task 2", "2026-03-01T12:00:00Z", 1800);

        TimeentryTimeEntryWithTask entry3 = getEntry("PROJECT-123 Task 1", "2026-03-02T08:00:00Z", 7200);

        given(timeEntriesApi.listStreamTimeEntriesRangeWithOrg(
                any(), any(), any(), any(), eq(Optional.empty()), eq(Optional.empty()), eq(Optional.empty()), eq(Optional.empty()), eq(Optional.empty())
        )).willReturn(Flux.just(entry1, entry2, entry3));

        StepVerifier.create(togglClient.summarizeEntries(YearMonth.of(2026, Month.MARCH)))
                .assertNext(summary -> {
                    assertThat(summary.description()).isEqualTo("PROJECT-123 Task 1");

                    assertThat(summary.dailyDurations())
                            .containsExactlyInAnyOrderEntriesOf(Map.of(
                                    LocalDate.of(2026, 3, 1), Duration.ofHours(1),
                                    LocalDate.of(2026, 3, 2), Duration.ofHours(2)
                            ));
                })
                .assertNext(summary -> {
                    assertThat(summary.description()).isEqualTo("PROJECT-124 Task 2");

                    assertThat(summary.dailyDurations())
                            .containsExactly(Map.entry(
                                    LocalDate.of(2026, 3, 1), Duration.ofMinutes(30)
                            ));
                })
                .verifyComplete();
    }

    private static @NonNull TimeentryTimeEntryWithTask getEntry(String description, String createdAt, int duration) {
        TimeentryTimeEntryWithTask entry = new TimeentryTimeEntryWithTask();
        entry.setTask(
                new ModelsTask()
                        .description(description)
        );
        entry.setCreatedAt(createdAt);
        entry.setDuration(duration);
        return entry;
    }
}
