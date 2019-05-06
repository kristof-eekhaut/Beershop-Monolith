package be.ordina.beershop.domain;

public interface Identifier<T> extends ValueObject {

    T getValue();
}
