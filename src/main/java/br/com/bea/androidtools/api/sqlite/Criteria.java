package br.com.bea.androidtools.api.sqlite;

import java.util.List;

public interface Criteria {

    static enum Expression {
        AND("(%s AND %s)"),
        BETWEEN("BETWEEN ? AND ?"),
        EQ(" = "),
        GE(" >= "),
        GT(" > "),
        IN(" IN (%s) "),
        IS_NOT_NULL(" IS NOT NULL "),
        IS_NULL(" IS NULL "),
        LE(" <= "),
        LIKE(" LIKE "),
        LT(" < "),
        NE(" != "),
        NOT("NOT (%s)"),
        OR(" (%s OR %s) ");

        private final String value;

        Expression(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    StringBuilder buildQuery(final StringBuilder builder);

    List<Object> getValues();

    void translate(final List<Class<?>> classes);
}
