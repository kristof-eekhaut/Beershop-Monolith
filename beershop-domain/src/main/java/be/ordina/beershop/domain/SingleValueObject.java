package be.ordina.beershop.domain;

import static java.util.Objects.requireNonNull;

public abstract class SingleValueObject<T> extends AbstractValueObject {

    private final T value;

    protected SingleValueObject(T value) {
        this.value = requireNonNull(value);
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
