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
    @Mapping(target = "sellerName", source = "issuer", qualifiedByName = "mapEntityName")
    @Mapping(target = "sellerTaxNo", source = "issuer", qualifiedByName = "mapEntityTaxNo")
    @Mapping(target = "buyerName", source = "recipient", qualifiedByName = "mapEntityName")
    @Mapping(target = "buyerTaxNo", source = "recipient", qualifiedByName = "mapEntityTaxNo")
    @Mapping(target = "buyerEmail", source = "recipient", qualifiedByName = "mapEntityEmail")
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

    @Named("mapEntityName")
    default String mapEntityName(Entity entity) {
        if (entity instanceof Company company) {
            return company.getName();
        } else if (entity instanceof Person person) {
            return person.getFirstName() + " " + person.getLastName();
        }
        return null;
    }

    @Named("mapEntityTaxNo")
    default String mapEntityTaxNo(Entity entity) {
        if (entity == null || entity.getTaxIds() == null) {
            return null;
        }
        return entity.getTaxIds().stream()
                .filter(taxId -> taxId instanceof TaxId.PolishNIP)
                .map(taxId -> ((TaxId.PolishNIP) taxId).raw())
                .findFirst()
                .orElse(entity.getTaxIds().stream()
                        .filter(taxId -> taxId instanceof TaxId.EuropeanVatId)
                        .map(taxId -> ((TaxId.EuropeanVatId) taxId).raw())
                        .findFirst()
                        .orElse(null));
    }

    @Named("mapEntityEmail")
    default String mapEntityEmail(Entity entity) {
        if (entity == null || entity.getContactData() == null) {
            return null;
        }
        return entity.getContactData().stream()
                .filter(contactData -> contactData instanceof ContactData.Email)
                .map(contactData -> ((ContactData.Email) contactData).raw())
                .findFirst()
                .orElse(null);
    }
}
