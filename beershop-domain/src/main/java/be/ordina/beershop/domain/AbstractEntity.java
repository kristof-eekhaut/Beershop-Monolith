package be.ordina.beershop.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public abstract class AbstractEntity<ID extends Identifier> implements Entity<ID> {

    private final ID id;

    protected AbstractEntity(ID id) {
        this.id = requireNonNull(id);
    }

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity<?> that = (AbstractEntity<?>) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
