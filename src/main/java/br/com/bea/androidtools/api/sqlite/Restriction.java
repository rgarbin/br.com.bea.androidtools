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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Restriction implements Criteria {

    public static enum MatchMode {
        ANYWHERE,
        END,
        START;
    }

    public static Criteria and(final Restriction lhs, final Restriction rhs) {
        return new Restriction(lhs, rhs, Expression.AND);
    }

    public static Criteria eq(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.EQ);
    }

    public static Criteria eqProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.EQ);
    }

    public static Criteria ge(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.GE);
    }

    public static Criteria geProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.GE);
    }

    public static Criteria gt(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.GT);
    }

    public static Criteria gtProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.GT);
    }

    public static Criteria isNotNull(final String principalProperty) {
        return new Restriction(principalProperty, Expression.IS_NOT_NULL);
    }

    public static Criteria isNull(final String principalProperty) {
        return new Restriction(principalProperty, Expression.IS_NULL);
    }

    public static Criteria le(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.LE);
    }

    public static Criteria leProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.LE);
    }

    public static Criteria like(final String principalProperty, final Object value, final MatchMode matchMode) {
        return new Restriction(principalProperty, value, Expression.LIKE, matchMode);
    }

    public static Criteria lt(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.LT);
    }

    public static Criteria ltProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.LT);
    }

    public static Criteria ne(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.NE);
    }

    public static Criteria neProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.NE);
    }

    public static Criteria not(final Restriction expression) {
        return new Restriction(expression, Expression.NOT);
    }

    public static Criteria or(final Restriction lhs, final Restriction rhs) {
        return new Restriction(lhs, rhs, Expression.OR);
    }

    private final Expression expression;
    private Restriction lcriterion;
    private MatchMode matchMode;
    private String principalProperty;
    private Restriction rcriterion;

    private String targetProperty;
    private String value;

    public Restriction(final Restriction criterion, final Expression expression) {
        lcriterion = criterion;
        this.expression = expression;
    }

    public Restriction(final Restriction lcriterion, final Restriction rcriterion, final Expression expression) {
        this.lcriterion = lcriterion;
        this.rcriterion = rcriterion;
        this.expression = expression;
    }

    public Restriction(final String principalProperty, final Expression expression) {
        this.principalProperty = principalProperty;
        this.expression = expression;
    }

    public Restriction(final String principalProperty, final Object value, final Expression expression) {
        this.principalProperty = principalProperty;
        this.value = String.valueOf(value);
        this.expression = expression;
    }

    public Restriction(final String principalProperty,
                       final Object value,
                       final Expression expression,
                       final MatchMode matchMode) {
        this.value = String.valueOf(value);
        this.expression = expression;
        this.matchMode = matchMode;
    }

    public Restriction(final String principalProperty, final String targetProperty, final Expression expression) {
        this.principalProperty = principalProperty;
        this.targetProperty = targetProperty;
        this.expression = expression;
    }

    @Override
    public StringBuilder buildQuery(final StringBuilder builder) {
        switch (expression) {
            case AND:
                builder.append(String.format(expression.toString(), lcriterion.buildQuery(new StringBuilder()),
                                             rcriterion.buildQuery(new StringBuilder())));
                break;
            case BETWEEN:
                break;
            case IS_NOT_NULL:
                builder.append(principalProperty).append(expression);
                break;
            case IS_NULL:
                builder.append(principalProperty).append(expression);
                break;
            case NOT:
                builder.append(String.format(expression.toString(), lcriterion.buildQuery(new StringBuilder())));
                break;
            case OR:
                builder.append(String.format(expression.toString(), lcriterion.buildQuery(new StringBuilder()),
                                             rcriterion.buildQuery(new StringBuilder())));
                break;
            case LIKE:
                builder.append(principalProperty).append(expression).append(" ? ");
                switch (matchMode) {
                    case ANYWHERE:
                        value = new StringBuilder().append("%").append(value).append("%").toString();
                        break;
                    case END:
                        value = new StringBuilder().append(value).append("%").toString();
                        break;
                    case START:
                        value = new StringBuilder().append("%").append(value).toString();
                        break;
                }
                break;
            default:
                builder.append(principalProperty).append(expression).append(null == targetProperty
                                                                                                  ? " ? "
                                                                                                  : targetProperty);
        }
        return builder;
    }

    @Override
    public List<String> getValues() {
        final List<String> values = new ArrayList<String>(0);
        if (null != lcriterion) values.addAll(lcriterion.getValues());
        if (null != rcriterion) values.addAll(rcriterion.getValues());
        if (null != value) values.addAll(Arrays.asList(String.valueOf(value)));
        return values;
    }
}
