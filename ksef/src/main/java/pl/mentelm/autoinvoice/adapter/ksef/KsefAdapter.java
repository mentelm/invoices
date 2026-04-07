package pl.mentelm.autoinvoice.adapter.ksef;

import pl.mentelm.autoinvoice.domain.IssueInvoice;
import pl.mentelm.autoinvoice.model.Invoice;
import reactor.core.publisher.Mono;

class KsefAdapter implements IssueInvoice {


    @Override
    public Mono<IssueInvoiceResult> issue(Invoice invoice) {
        return null;
    }
}
