package be.ordina.beershop.repository;

import be.ordina.beershop.domain.AggregateRoot;
import be.ordina.beershop.domain.Identifier;
import be.ordina.beershop.domain.Repository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public abstract class AbstractJPARepository<AR_ID extends Identifier, AR extends AggregateRoot<AR_ID>, JPA_ID, JPA_ENTITY>
        implements Repository<AR_ID, AR> {

    private JpaRepository<JPA_ENTITY, JPA_ID> jpaRepository;
    private ApplicationEventPublisher eventPublisher;

    protected AbstractJPARepository(JpaRepository<JPA_ENTITY, JPA_ID> jpaRepository,
                                    ApplicationEventPublisher eventPublisher) {
        this.jpaRepository = requireNonNull(jpaRepository);
        this.eventPublisher = requireNonNull(eventPublisher);
    }

    @Override
    public final void add(AR aggregateRoot) {
        jpaRepository.save(createJPAEntity(aggregateRoot));

        publishEvents(aggregateRoot);
    }

    @Override
    public final void update(AR aggregateRoot) {
        JPA_ID jpaId = mapToJPAId(aggregateRoot.getId());
        JPA_ENTITY jpaEntity = jpaRepository.getOne(jpaId);
        mapToJPAEntity(aggregateRoot, jpaEntity);
        jpaRepository.save(jpaEntity);

        publishEvents(aggregateRoot);
    }

    @Override
    public Optional<AR> findById(AR_ID aggregateRootId) {
        JPA_ID jpaId = mapToJPAId(aggregateRootId);
        return jpaRepository.findById(jpaId)
                .map(this::mapToDomain);
    }

    @Override
    public Page<AR> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(this::mapToDomain);
    }

    protected abstract JPA_ENTITY createEmptyJPAEntity(AR_ID aggregateRootId);
    protected abstract void mapToJPAEntity(AR from, JPA_ENTITY to);
    protected abstract AR mapToDomain(JPA_ENTITY jpaEntity);
    protected abstract JPA_ID mapToJPAId(AR_ID aggregateRootId);

    private void publishEvents(AR aggregateRoot) {
        aggregateRoot.getRegisteredEvents()
                .forEach(eventPublisher::publishEvent);
    }

    private JPA_ENTITY createJPAEntity(AR from) {
        JPA_ENTITY emptyJpaEntity = createEmptyJPAEntity(from.getId());
        mapToJPAEntity(from, emptyJpaEntity);
        return emptyJpaEntity;
    }
}
