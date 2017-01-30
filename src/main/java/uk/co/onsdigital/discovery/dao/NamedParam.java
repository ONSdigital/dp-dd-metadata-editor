package uk.co.onsdigital.discovery.dao;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class NamedParam {

    private String key;
    private Object value;

    public NamedParam(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NamedParam that = (NamedParam) o;

        return new EqualsBuilder()
                .append(getKey(), that.getKey())
                .append(getValue(), that.getValue())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getKey())
                .append(getValue())
                .toHashCode();
    }

    @Override
    public String toString() {
        return "[key=" + this.getKey() + ", value=" + this.getValue() + "]";
    }
}
