package pl.mentelm.autoinvoice.model.entity;

public sealed interface ContactData {

    record Email(String raw) implements ContactData {}
    record Phone(String raw) implements ContactData {}

}
