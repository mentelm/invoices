package pl.mentelm.autoinvoice.fakturownia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@RequiredArgsConstructor(staticName = "of")
class InvoiceRequest {

    @JsonProperty("api_token")
    String apiToken;

    InvoiceDto invoice;
}
