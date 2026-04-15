package pl.mentelm.autoinvoice.model.time;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

public record TimeEntrySummary(
        String description,
        Map<LocalDate, Duration> dailyDurations
) {
}
