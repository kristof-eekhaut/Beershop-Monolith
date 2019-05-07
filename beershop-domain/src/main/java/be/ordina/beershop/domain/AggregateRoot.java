package be.ordina.beershop.domain;

import java.util.List;

public interface AggregateRoot<ID extends Identifier> extends Entity<ID> {

    List<DomainEvent> getRegisteredEvents();
    void clearRegisteredEvents();
}
