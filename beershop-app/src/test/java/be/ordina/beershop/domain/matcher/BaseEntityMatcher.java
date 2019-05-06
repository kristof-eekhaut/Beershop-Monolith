package be.ordina.beershop.domain.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.equalTo;

public abstract class BaseEntityMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    protected T objectToMatch;

    protected BaseEntityMatcher(T objectToMatch) {
        this.objectToMatch = objectToMatch;
    }

    @Override
    protected final boolean matchesSafely(T other, Description description) {
        description.appendText("was ");
        describeTo(description, other);

        return matchesSafely(other);
    }

    protected abstract boolean matchesSafely(T other);

    @Override
    public final void describeTo(Description description) {
        describeTo(description, objectToMatch);
    }

    private void describeTo(Description description, T object) {
        description.appendText("\n").appendText(object.getClass().getSimpleName() + "{ ");
        describeToEntity(description, object);
        description.appendText("\n}");
    }

    protected abstract void describeToEntity(Description description, T object);


    protected final boolean isEqual(Object value1, Object value2) {
        return equalTo(value1).matches(value2);
    }

    protected final Description appendField(Description description, String fieldName, Object value) {
        return description.appendText(appendFieldName(fieldName)).appendText("" + value);
    }

    protected final Description appendDescriptionOf(Description description, String fieldName, Matcher<?> matcher) {
        return description.appendText(appendFieldName(fieldName)).appendDescriptionOf(matcher);
    }

    protected final String appendFieldName(String fieldName) {
        return "\n\t" + fieldName + ": ";
    }
}
