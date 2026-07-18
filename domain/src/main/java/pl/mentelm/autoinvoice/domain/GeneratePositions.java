package pl.mentelm.autoinvoice.domain;

import pl.mentelm.autoinvoice.model.entity.Party;
import pl.mentelm.autoinvoice.model.position.InvoicePosition;
import reactor.core.publisher.Flux;

public interface GeneratePositions {

    Flux<InvoicePosition> generatePositionsFor(Party client);
}
