package pl.mentelm.autoinvoice.fakturownia;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

import java.util.List;

// documentation at https://github.com/fakturownia/API
interface FakturowniaClient {

    @GetExchange(
            url = "/invoices.json",
            accept = MediaType.APPLICATION_JSON_VALUE
    )
    Mono<List<InvoiceDto>> getInvoices(
            @RequestParam("api_token") String apiToken,
            @RequestParam(name = "period", required = false) String period
    );

    @GetExchange(
            url = "/invoices/{id}.json",
            accept = MediaType.APPLICATION_JSON_VALUE
    )
    Mono<InvoiceDto> getInvoice(
            @PathVariable Long id,
            @RequestParam("api_token") String apiToken
    );

    @GetExchange(url = "/invoices/{id}.pdf")
    Mono<byte[]> getInvoicePdf(
            @PathVariable Long id,
            @RequestParam("api_token") String apiToken
    );

    @PostExchange(
            url = "/invoices.json",
            contentType = MediaType.APPLICATION_JSON_VALUE,
            accept = MediaType.APPLICATION_JSON_VALUE
    )
    Mono<InvoiceDto> createInvoice(
            @RequestBody InvoiceRequest request
    );
}
