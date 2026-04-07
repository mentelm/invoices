package pl.mentelm.autoinvoice.adapter.ksef;

import pl.gov.crd.wzor._2025._06._25._13775.Faktura;
import pl.mentelm.autoinvoice.model.Invoice;
import pl.mentelm.autoinvoice.model.entity.Entity;

public class InvoiceMapper {

    public Faktura toKsefInvoice(Invoice invoice) {
        var faktura = new Faktura();
        faktura.setPodmiot1(sprzedawca(invoice.getIssuer()));
        faktura.setPodmiot2(nabywca(invoice.getRecipient()));

        return faktura;
    }

    private Faktura.Podmiot1 sprzedawca(Entity issuer) {
        var sprzedawca = new Faktura.Podmiot1();

        return sprzedawca;
    }

    private Faktura.Podmiot2 nabywca(Entity recipient) {
        var nabywca = new Faktura.Podmiot2();
//        nabywca.setAdres();
        return nabywca;
    }
}
