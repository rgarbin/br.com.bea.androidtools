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
import java.util.LinkedList;
import java.util.List;
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

    private final List<String> groupBy = new LinkedList<String>();
    private final Limit limit = new Limit();
    private final List<String> orderBy = new LinkedList<String>();
    private final List<Criteria> selection = new LinkedList<Criteria>();
    private Class<?> targetClass;

    private QueryBuilder() {
    }

    Cursor build(final SQLiteDatabase sqlite) {
        return sqlite.query(true, targetClass.getAnnotation(Table.class).name(), buildColumns(), buildSelection(),
                            selectionValues(), buildGroupBy(), null, buildOrderBy(), buildLimit(), null);
    }

    @SuppressWarnings("unchecked")
    private String[] buildColumns() {
        final List<String> columns = new ArrayList<String>(0);
        for (final Field field : EntityUtils.columnFields((Class<Entity<?>>) targetClass))
            if (field.isAnnotationPresent(Column.class)) columns.add(field.getAnnotation(Column.class).name());
        return columns.toArray(new String[columns.size()]);
    }

    private String buildGroupBy() {
        if (groupBy.isEmpty()) return null;
        final StringBuilder builder = new StringBuilder();
        for (final Iterator<String> iterator = groupBy.iterator(); iterator.hasNext();) {
            builder.append(iterator.next());
            if (iterator.hasNext()) builder.append(", ");
        }
        return builder.toString();
    }

    private String buildLimit() {
        if (limit.isEmpty()) return null;
        final StringBuilder builder = new StringBuilder();
        return builder.append(limit.getFirstResult()).append(", ").append(limit.getMaxResult()).toString();
    }

    private String buildOrderBy() {
        if (orderBy.isEmpty()) return null;
        final StringBuilder builder = new StringBuilder();
        for (final Iterator<String> iterator = orderBy.iterator(); iterator.hasNext();) {
            builder.append(iterator.next());
            if (iterator.hasNext()) builder.append(", ");
        }
        return builder.toString();
    }

    private String buildSelection() {
        if (selection.isEmpty()) return null;
        final StringBuilder builder = new StringBuilder();
        for (final Iterator<Criteria> iterator = selection.iterator(); iterator.hasNext();) {
            final Criteria criteria = iterator.next();
            criteria.buildQuery(builder);
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

    public QueryBuilder groupBy(final String property, final String... properties) {
        groupBy.add(property);
        groupBy.addAll(Arrays.asList(properties));
        return this;
    }

    public QueryBuilder limit(final Long firstResult, final Long maxResult) {
        limit.setFirstResult(firstResult);
        limit.setMaxResult(maxResult);
        return this;
    }

    public QueryBuilder orderBy(final String property, final String... properties) {
        orderBy.add(property);
        orderBy.addAll(Arrays.asList(properties));
        return this;
    }

    private String[] selectionValues() {
        if (selection.isEmpty()) return null;
        final List<String> values = new LinkedList<String>();
        for (final Criteria criteria : selection) {
            values.addAll(criteria.getValues());
        }
        return values.toArray(new String[values.size()]);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(true).append("\n").append(targetClass.getAnnotation(Table.class).name())
            .append("\n").append(Arrays.toString(buildColumns())).append("\n").append(buildSelection()).append("\n")
            .append(Arrays.toString(selectionValues())).append("\n").append(buildGroupBy()).append("\n").append("")
            .append("\n").append(buildOrderBy()).append("\n").append(buildLimit()).toString();
    }

    public QueryBuilder where(final Criteria... criteria) {
        selection.clear();
        selection.addAll(Arrays.asList(criteria));
        return this;
    }

    public QueryBuilder where(final List<Criteria> criteria) {
        selection.clear();
        selection.addAll(criteria);
        return this;
    }
}
