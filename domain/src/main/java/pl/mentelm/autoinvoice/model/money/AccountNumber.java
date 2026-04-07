package pl.mentelm.autoinvoice.model.money;

import jakarta.validation.constraints.Pattern;

public sealed interface AccountNumber {

    record IBAN(
            @Pattern(regexp = "^[A-Z]{2}[\\d]{28}$")
            String raw
    ) implements AccountNumber {

    }
}
