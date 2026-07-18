package pl.mentelm.autoinvoice.fakturownia;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.mentelm.autoinvoice.model.Invoice;
import pl.mentelm.autoinvoice.model.InvoiceId;
import pl.mentelm.autoinvoice.model.entity.*;
import pl.mentelm.autoinvoice.model.money.Payment;

import java.time.LocalDate;

@Mapper(componentModel = "spring", uses = {InvoicePositionMapper.class})
interface InvoiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kind", constant = "vat")
    @Mapping(target = "number", source = "invoiceId", qualifiedByName = "mapInvoiceId")
    @Mapping(target = "sellDate", source = "serviceExecutionDate")
    @Mapping(target = "issueDate", source = "issueDate")
    @Mapping(target = "paymentTo", source = "payment", qualifiedByName = "mapPaymentTo")
    @Mapping(target = "sellerName", source = "issuer", qualifiedByName = "mapPartyName")
    @Mapping(target = "sellerTaxNo", source = "issuer", qualifiedByName = "mapPartyTaxNo")
    @Mapping(target = "buyerName", source = "recipient", qualifiedByName = "mapPartyName")
    @Mapping(target = "buyerTaxNo", source = "recipient", qualifiedByName = "mapPartyTaxNo")
    @Mapping(target = "buyerEmail", source = "recipient", qualifiedByName = "mapPartyEmail")
    @Mapping(target = "positions", source = "positions")
    InvoiceDto toDto(Invoice invoice);

    @Named("mapInvoiceId")
    default String mapInvoiceId(InvoiceId invoiceId) {
        return invoiceId != null ? invoiceId.raw() : null;
    }

    @Named("mapPaymentTo")
    default LocalDate mapPaymentTo(Payment payment) {
        if (payment instanceof Payment.WireTransfer wireTransfer) {
            return wireTransfer.paymentDueDate();
        }
        return null;
    }

    @Named("mapPartyName")
    default String mapPartyName(Party party) {
        if (party instanceof Company company) {
            return company.getName();
        } else if (party instanceof Person person) {
            return person.getFirstName() + " " + person.getLastName();
        }
        return null;
    }

    @Named("mapPartyTaxNo")
    default String mapPartyTaxNo(Party party) {
        if (party == null || party.getTaxIds() == null) {
            return null;
        }
        return party.getTaxIds().stream()
                .filter(taxId -> taxId instanceof TaxId.PolishNIP)
                .map(taxId -> ((TaxId.PolishNIP) taxId).raw())
                .findFirst()
                .orElse(party.getTaxIds().stream()
                        .filter(taxId -> taxId instanceof TaxId.EuropeanVatId)
                        .map(taxId -> ((TaxId.EuropeanVatId) taxId).raw())
                        .findFirst()
                        .orElse(null));
    }

    @Named("mapPartyEmail")
    default String mapPartyEmail(Party party) {
        if (party == null || party.getContactData() == null) {
            return null;
        }
        return party.getContactData().stream()
                .filter(contactData -> contactData instanceof ContactData.Email)
                .map(contactData -> ((ContactData.Email) contactData).raw())
                .findFirst()
                .orElse(null);
    }
}
