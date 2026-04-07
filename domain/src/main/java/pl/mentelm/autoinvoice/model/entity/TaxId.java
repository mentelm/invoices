package pl.mentelm.autoinvoice.model.entity;

import jakarta.validation.constraints.Pattern;

public sealed interface TaxId {

    record EuropeanVatId(String raw) implements TaxId {}

    record PolishNIP(
            @Pattern(regexp = "^\\d{10}$")
            String raw
    ) implements TaxId {}
}
