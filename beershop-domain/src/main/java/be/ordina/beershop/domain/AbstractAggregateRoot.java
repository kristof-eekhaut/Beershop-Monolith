package be.ordina.beershop.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAggregateRoot<ID extends Identifier<?>> extends AbstractEntity<ID>
        implements AggregateRoot<ID> {

    private List<DomainEvent> registeredDomainEvents = new ArrayList<>();

    protected AbstractAggregateRoot(ID id) {
        super(id);
    }

    protected final void registerEvent(DomainEvent event) {
        this.registeredDomainEvents.add(event);
    }

    @Override
    public final List<DomainEvent> getRegisteredEvents() {
        return new ArrayList<>(registeredDomainEvents);
    }

    @Override
    public final void clearRegisteredEvents() {
        registeredDomainEvents.clear();
    }
}
