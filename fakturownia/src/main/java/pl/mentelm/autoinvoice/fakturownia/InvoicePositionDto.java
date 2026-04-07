package pl.mentelm.autoinvoice.fakturownia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
class InvoicePositionDto {

    String name;
    String tax;

    @JsonProperty("total_price_gross")
    BigDecimal totalPriceGross;

    BigDecimal quantity;

    @JsonProperty("product_id")
    Long productId;
}
