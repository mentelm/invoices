package pl.mentelm.autoinvoice.fakturownia;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.mentelm.autoinvoice.model.position.InvoicePosition;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

@Mapper(componentModel = "spring")
interface InvoicePositionMapper {

    @Mapping(target = "totalPriceGross", source = "position", qualifiedByName = "calculateTotalPriceGross")
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "tax", source = "taxRate")
    InvoicePositionDto toPositionDto(InvoicePosition position);

    @Named("calculateTotalPriceGross")
    default BigDecimal calculateTotalPriceGross(InvoicePosition position) {
        if (position == null || position.unitNetPrice() == null || position.quantity() == null || position.taxRate() == null) {
            return null;
        }
        BigDecimal netTotal = position.unitNetPrice().multiply(position.quantity());
        BigDecimal taxMultiplier = BigDecimal.ONE.add(position.taxRate().divide(new BigDecimal("100")));
        return netTotal.multiply(taxMultiplier).setScale(2, HALF_UP);
    }
}
