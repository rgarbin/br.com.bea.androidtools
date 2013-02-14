package br.com.bea.androidtools.api.sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.bea.androidtools.api.annotations.Column;
import br.com.bea.androidtools.api.annotations.Table;
import br.com.bea.androidtools.api.model.Entity;
import br.com.bea.androidtools.api.model.EntityUtils;

public final class QueryBuilder {

    public static synchronized QueryBuilder select() {
        return new QueryBuilder();
    }

    private final List<String> orderBy = new LinkedList<String>();
    private final Map<String, Object> selection = new LinkedHashMap<String, Object>(0);
    private Class<?> targetClass;

    private QueryBuilder() {
    }

    @SuppressWarnings("unchecked")
    Cursor build(final SQLiteDatabase sqlite) {
        final List<String> columns = new ArrayList<String>(0);
        for (final Field field : EntityUtils.columnFields((Class<Entity<?>>) targetClass))
            if (field.isAnnotationPresent(Column.class)) columns.add(field.getAnnotation(Column.class).name());

        return sqlite.query(targetClass.getAnnotation(Table.class).name(), columns.toArray(new String[columns.size()]),
                            buildSelection(), selection.size() > 0 ? selection.values().toArray(new String[selection
                                                                                                    .size()]) : null,
                            null, null, buildOrderBy());
    }

    private String buildOrderBy() {
        final StringBuilder builder = new StringBuilder();
        for (final Iterator<String> iterator = orderBy.iterator(); iterator.hasNext();) {
            builder.append(iterator.next());
            if (iterator.hasNext()) builder.append(", ");
        }
        return builder.toString();
    }

    private String buildSelection() {
        final StringBuilder builder = new StringBuilder();
        for (final Iterator<Entry<String, Object>> iterator = selection.entrySet().iterator(); iterator.hasNext();) {
            final Entry<String, Object> entry = iterator.next();
            builder.append(entry.getKey()).append(" = ? ");
            if (iterator.hasNext()) builder.append(", ");
        }
        return builder.toString();
    }

    public <E extends Entity<?>> QueryBuilder from(final Class<E> targetClass) {
        this.targetClass = targetClass;
        return this;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public QueryBuilder orderBy(final String order, final String... orders) {
        orderBy.add(order);
        orderBy.addAll(Arrays.asList(orders));
        return this;
    }

    public QueryBuilder whereEquals(final String name, final Object value) {
        selection.put(name, value);
        return this;
    }
}
