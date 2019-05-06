package be.ordina.beershop.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface Repository<ID extends Identifier<?>, AR extends AggregateRoot<ID>> {

    ID nextId();

    void add(AR aggregateRoot);
    void update(AR aggregateRoot);
    void remove(AR aggregateRoot);
    void remove(ID aggregateRootId);

    Optional<AR> findById(ID aggregateRootId);
    Page<AR> findAll(Pageable pageable);
}
