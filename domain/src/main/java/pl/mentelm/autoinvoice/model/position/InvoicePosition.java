package pl.mentelm.autoinvoice.model.position;

import java.math.BigDecimal;
import java.util.Optional;

public record InvoicePosition(
        BigDecimal unitNetPrice,
        BigDecimal quantity,
        BigDecimal taxRate,
        Unit unit,
        String name,
        Optional<String> description
) {
}
