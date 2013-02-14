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

    public static Restriction and(final Restriction lhs, final Restriction rhs) {
        return new Restriction(lhs, rhs, Expression.AND);
    }

    public static Restriction eq(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.EQ);
    }

    public static Restriction eqProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.EQ);
    }

    public static Restriction ge(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.GE);
    }

    public static Restriction geProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.GE);
    }

    public static Restriction gt(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.GT);
    }

    public static Restriction gtProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.GT);
    }

    public static Restriction in(final String principalProperty, final Object... values) {
        return new Restriction(principalProperty, values, Expression.IN);
    }

    public static Restriction isNotNull(final String principalProperty) {
        return new Restriction(principalProperty, Expression.IS_NOT_NULL);
    }

    public static Restriction isNull(final String principalProperty) {
        return new Restriction(principalProperty, Expression.IS_NULL);
    }

    public static Restriction le(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.LE);
    }

    public static Restriction leProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.LE);
    }

    public static Restriction like(final String principalProperty, final Object value, final MatchMode matchMode) {
        return new Restriction(principalProperty, value, Expression.LIKE, matchMode);
    }

    public static Restriction lt(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.LT);
    }

    public static Restriction ltProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.LT);
    }

    public static Restriction ne(final String principalProperty, final Object value) {
        return new Restriction(principalProperty, value, Expression.NE);
    }

    public static Restriction neProperty(final String principalProperty, final String targetProperty) {
        return new Restriction(principalProperty, targetProperty, Expression.NE);
    }

    public static Restriction not(final Restriction expression) {
        return new Restriction(expression, Expression.NOT);
    }

    public static Restriction or(final Restriction lhs, final Restriction rhs) {
        return new Restriction(lhs, rhs, Expression.OR);
    }

    private final Expression expression;

    private Restriction lcriterion;

    private MatchMode matchMode;

    private String principalColumn;

    private Restriction rcriterion;

    private String targetColumn;

    private Object value;

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
        this.expression = expression;
    }

    public Restriction(final String principalProperty, final Object value, final Expression expression) {
        this.value = value;
        this.expression = expression;
    }

    public Restriction(final String principalProperty,
                       final Object value,
                       final Expression expression,
                       final MatchMode matchMode) {
        this.value = value;
        this.expression = expression;
        this.matchMode = matchMode;
    }

    public Restriction(final String principalProperty, final String targetProperty, final Expression expression) {
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
            case IN:
                final StringBuilder inBuilder = new StringBuilder();
                if (value instanceof Object[]) for (int i = 0; i < ((Object[]) value).length; i++) {
                    if (i != 0) inBuilder.append(",");
                    inBuilder.append("?");
                }
                builder.append(principalColumn).append(String.format(Expression.IN.toString(), inBuilder.toString()));
                break;
            case IS_NOT_NULL:
                builder.append(principalColumn).append(expression);
                break;
            case IS_NULL:
                builder.append(principalColumn).append(expression);
                break;
            case NOT:
                builder.append(String.format(expression.toString(), lcriterion.buildQuery(new StringBuilder())));
                break;
            case OR:
                builder.append(String.format(expression.toString(), lcriterion.buildQuery(new StringBuilder()),
                                             rcriterion.buildQuery(new StringBuilder())));
                break;
            case LIKE:
                builder.append(principalColumn).append(expression).append(" ? ");
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
                builder.append(principalColumn).append(expression).append(null == targetColumn ? " ? " : targetColumn);
        }
        return builder;
    }

    @Override
    public List<Object> getValues() {
        final List<Object> values = new ArrayList<Object>(0);
        if (null != lcriterion) values.addAll(lcriterion.getValues());
        if (null != rcriterion) values.addAll(rcriterion.getValues());
        if (null != value) values.addAll(Arrays.asList(value));
        return values;
    }

    @Override
    public void translate(final List<Class<?>> classes) {
        if (null != lcriterion) lcriterion.translate(classes);
        if (null != rcriterion) rcriterion.translate(classes);
        // if (null != targetProperty) targetColumn = Utils.translate(classes,
        // targetProperty);
        // principalColumn = Utils.translate(classes, principalProperty);
    }

}
