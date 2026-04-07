package pl.mentelm.autoinvoice.fakturownia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@Jacksonized
class InvoiceDto {

    Long id;
    String kind;
    String number;

    @JsonProperty("sell_date")
    LocalDate sellDate;

    @JsonProperty("issue_date")
    LocalDate issueDate;

    @JsonProperty("payment_to")
    LocalDate paymentTo;

    @JsonProperty("seller_name")
    String sellerName;

    @JsonProperty("seller_tax_no")
    String sellerTaxNo;

    @JsonProperty("buyer_name")
    String buyerName;

    @JsonProperty("buyer_tax_no")
    String buyerTaxNo;

    @JsonProperty("buyer_email")
    String buyerEmail;

    List<InvoicePositionDto> positions;
}
