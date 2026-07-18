package pl.mentelm.autoinvoice.model.entity;

import lombok.Getter;

@Getter
public class Person extends Party {

    String firstName;
    String lastName;
    Address address;
}
