package pl.mentelm.autoinvoice.model.money;

import java.time.LocalDate;

public sealed interface Payment {

    record WireTransfer(
            BankAccountDetails bankAccountDetails,
            LocalDate paymentDueDate
    ) implements Payment {}
}
