package pl.mentelm.autoinvoice.model;

import lombok.Builder;
import lombok.Value;
import pl.mentelm.autoinvoice.model.entity.Entity;
import pl.mentelm.autoinvoice.model.money.Payment;
import pl.mentelm.autoinvoice.model.position.InvoicePosition;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Value
@Builder
public class Invoice {

    UUID id;
    InvoiceId invoiceId;

    Entity issuer;
    Entity recipient;
    Optional<Entity> payer;

    LocalDate issueDate;
    LocalDate serviceExecutionDate;

    Payment payment;
    List<InvoicePosition> positions;
}
