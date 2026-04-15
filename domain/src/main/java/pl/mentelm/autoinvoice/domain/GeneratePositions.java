package pl.mentelm.autoinvoice.domain;

import pl.mentelm.autoinvoice.model.entity.Entity;
import pl.mentelm.autoinvoice.model.position.InvoicePosition;
import reactor.core.publisher.Flux;

public interface GeneratePositions {

    Flux<InvoicePosition> generatePositionsFor(Entity client);
}
