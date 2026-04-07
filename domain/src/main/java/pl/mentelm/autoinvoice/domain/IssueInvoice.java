package pl.mentelm.autoinvoice.domain;

import pl.mentelm.autoinvoice.model.Invoice;
import reactor.core.publisher.Mono;

public interface IssueInvoice {

    Mono<IssueInvoiceResult> issue(Invoice invoice);

    sealed interface IssueInvoiceResult {
        record Success() implements IssueInvoiceResult {}
        record Failure(String reason) implements IssueInvoiceResult {}
    }
}
