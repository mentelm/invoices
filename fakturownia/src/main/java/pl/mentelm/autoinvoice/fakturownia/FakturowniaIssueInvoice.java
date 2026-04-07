package pl.mentelm.autoinvoice.fakturownia;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mentelm.autoinvoice.domain.IssueInvoice;
import pl.mentelm.autoinvoice.model.Invoice;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
class FakturowniaIssueInvoice implements IssueInvoice {

    private final FakturowniaClient fakturowniaClient;
    private final FakturowniaProperties properties;
    private final InvoiceMapper mapper;

    @Override
    public Mono<IssueInvoiceResult> issue(Invoice invoice) {
        var request = InvoiceRequest.of(
                properties.getApiToken(),
                mapper.toDto(invoice)
        );

        return fakturowniaClient.createInvoice(request)
                .map(response -> (IssueInvoiceResult) new IssueInvoiceResult.Success())
                .onErrorResume(throwable -> Mono.just(new IssueInvoiceResult.Failure(throwable.getMessage())));
    }
}
