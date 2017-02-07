package uk.co.onsdigital.discovery.dao.parameters;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * A key pair type to encapsulate named parameter values for use with named queries.
 */
public class NamedParam extends AbstractMap.SimpleEntry<String, Object> {

    public NamedParam(String key, Object value) {
        super(key, value);
    }

    /**
     * Provides a convenient way of creating {@link NamedParam}'s and adding them to a {@link List}
     */
    public static class ListBuilder {
        private List<NamedParam> list;

        public ListBuilder() {
            this.list = new ArrayList<>();
        }

        /**
         * Creates a new {@link NamedParam} and adds it to the list.
         * @param key the name of the parameter field.
         * @param value the value of the parameter to insert into the named query.
         * @return its self enabling method chaining.
         */
        public ListBuilder addParam(String key, Object value) {
            this.list.add(new NamedParam(key, value));
            return this;
        }

        /**
         * @return the list of {@link NamedParam} values.
         */
        public List<NamedParam> toList() {
            return this.list;
        }
    }
}
