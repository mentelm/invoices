package pl.mentelm.autoinvoice.model.entity;

import lombok.Getter;

import java.util.Set;

@Getter
public abstract class Party {

    Address address;
    Set<TaxId> taxIds;
    Set<ContactData> contactData;
}
