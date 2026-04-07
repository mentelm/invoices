package pl.mentelm.autoinvoice.fakturownia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InvoiceMother {

    public static InvoiceDto dummyInvoiceDto() {
        return InvoiceDto.builder()
                .number("FV/2026/04/01")
                .kind("vat")
                .sellDate(LocalDate.of(2026, 4, 1))
                .issueDate(LocalDate.of(2026, 4, 2))
                .paymentTo(LocalDate.of(2026, 4, 15))
                .sellerName("Seller Sp. z o.o.")
                .sellerTaxNo("5250000000")
                .buyerName("Buyer Sp. z o.o.")
                .buyerTaxNo("5251111111")
                .buyerEmail("buyer@example.com")
                .positions(List.of(
                        InvoicePositionDto.builder()
                                .name("Service A")
                                .tax("23")
                                .totalPriceGross(new BigDecimal("123.0"))
                                .quantity(BigDecimal.ONE)
                                .build()
                ))
                .build();
    }
}
