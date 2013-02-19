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
