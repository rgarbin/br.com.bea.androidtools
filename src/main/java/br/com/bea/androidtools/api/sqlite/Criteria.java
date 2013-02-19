/*
The MIT License (MIT)
Copyright (c) 2013 B&A Tecnologia and Collaborators

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions 
of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
 */

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
