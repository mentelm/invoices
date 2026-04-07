package pl.mentelm.autoinvoice.model.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Address {

    @NotBlank
    String street;

    @NotBlank
    String houseNumber;
    String apartmentNumber;

    @NotBlank
    String city;

    @NotBlank
    String zipCode;

    @NotBlank
    String postOffice;

    @NotBlank
    String country;
}
