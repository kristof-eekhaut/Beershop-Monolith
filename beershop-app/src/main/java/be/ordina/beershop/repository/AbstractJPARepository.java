package be.ordina.beershop.repository;

import be.ordina.beershop.domain.AggregateRoot;
import be.ordina.beershop.domain.Identifier;
import be.ordina.beershop.domain.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public abstract class AbstractJPARepository<AR_ID extends Identifier<JPA_ID>, AR extends AggregateRoot<AR_ID>, JPA_ID, JPA_ENTITY>
        implements Repository<AR_ID, AR> {

    private JpaRepository<JPA_ENTITY, JPA_ID> jpaRepository;

    protected AbstractJPARepository(JpaRepository<JPA_ENTITY, JPA_ID> jpaRepository) {
        this.jpaRepository = requireNonNull(jpaRepository);
    }

    @Override
    public final void add(AR aggregateRoot) {
        jpaRepository.save(createJPAEntity(aggregateRoot));
    }

    @Override
    public final void update(AR aggregateRoot) {
        JPA_ENTITY jpaEntity = jpaRepository.getOne(aggregateRoot.getId().getValue());
        jpaRepository.save(mapToJPAEntity(aggregateRoot, jpaEntity));
    }

    @Override
    public void remove(AR_ID aggregateRootId) {
        JPA_ENTITY jpaEntity = jpaRepository.getOne(aggregateRootId.getValue());
        jpaRepository.delete(jpaEntity);
    }

    @Override
    public void remove(AR aggregateRoot) {
        remove(aggregateRoot.getId());
    }

    @Override
    public Optional<AR> findById(AR_ID aggregateRootId) {
        return jpaRepository.findById(aggregateRootId.getValue())
                .map(this::mapToDomain);
    }

    @Override
    public Page<AR> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(this::mapToDomain);
    }

    protected abstract JPA_ENTITY createEmptyJPAEntity(AR_ID aggregateRootId);
    protected abstract JPA_ENTITY mapToJPAEntity(AR from, JPA_ENTITY to);
    protected abstract AR mapToDomain(JPA_ENTITY jpaEntity);

    protected JpaRepository<JPA_ENTITY, JPA_ID> getJpaRepository() {
        return jpaRepository;
    }

    private JPA_ENTITY createJPAEntity(AR from) {
        JPA_ENTITY emptyJpaEntity = createEmptyJPAEntity(from.getId());
        return mapToJPAEntity(from, emptyJpaEntity);
    }
}
