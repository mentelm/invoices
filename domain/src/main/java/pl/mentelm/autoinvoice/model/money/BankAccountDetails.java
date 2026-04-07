package pl.mentelm.autoinvoice.model.money;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BankAccountDetails {

    @NotBlank
    String bankName;
    AccountNumber accountNumber;

    @Pattern(regexp = "^[A-Z0-9]{8,11}$")
    String swiftCode;
    Currency currency;
}
