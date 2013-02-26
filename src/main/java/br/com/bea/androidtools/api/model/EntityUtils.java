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

package br.com.bea.androidtools.api.model;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import br.com.bea.androidtools.api.annotations.Column;
import br.com.bea.androidtools.api.annotations.Metadata;

public final class EntityUtils {
    private static final Map<Integer, List<Field>> columnsCache = new LinkedHashMap<Integer, List<Field>>();
    private static final Map<Integer, List<Field>> metadatasCache = new LinkedHashMap<Integer, List<Field>>();

    public static final <E extends Entity<?>> List<Field> columnFields(final Class<E> targetClass) {
        if (null == EntityUtils.columnsCache.get(targetClass.getName().hashCode())) {
            final List<Field> fields = new LinkedList<Field>();
            for (final Field field : targetClass.getDeclaredFields())
                if (field.isAnnotationPresent(Column.class)) fields.add(field);
            EntityUtils.columnsCache.put(targetClass.getName().hashCode(), fields);
        }
        return EntityUtils.columnsCache.get(targetClass.getName().hashCode());
    }

    public static final <E extends ValueObject> List<Field> metadataFields(final Class<E> targetClass) {
        if (null == EntityUtils.metadatasCache.get(targetClass.getName().hashCode())) {
            final List<Field> fields = new LinkedList<Field>();
            for (final Field field : targetClass.getDeclaredFields())
                if (field.isAnnotationPresent(Metadata.class)) fields.add(field);
            EntityUtils.metadatasCache.put(targetClass.getName().hashCode(), fields);
        }
        return EntityUtils.metadatasCache.get(targetClass.getName().hashCode());
    }
}
