package be.ordina.beershop.domain;

public interface Entity<ID extends Identifier<?>> {

    ID getId();
}
