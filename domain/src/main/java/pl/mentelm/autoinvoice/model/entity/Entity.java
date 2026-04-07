package pl.mentelm.autoinvoice.model.entity;

import lombok.Getter;

import java.util.Set;

@Getter
public abstract class Entity {

    Address address;
    Set<TaxId> taxIds;
    Set<ContactData> contactData;
}
