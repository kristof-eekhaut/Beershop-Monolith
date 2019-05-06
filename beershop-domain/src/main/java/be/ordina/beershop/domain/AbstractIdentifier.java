package be.ordina.beershop.domain;

public abstract class AbstractIdentifier<T> extends SingleValueObject<T>
        implements Identifier<T> {

    public AbstractIdentifier(T value) {
        super(value);
    }

    @Override
    public T getValue() {
        return super.getValue();
    }
}
