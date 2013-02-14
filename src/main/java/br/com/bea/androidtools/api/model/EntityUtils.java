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
        if (null == columnsCache.get(targetClass.getName().hashCode())) {
            final List<Field> fields = new LinkedList<Field>();
            for (final Field field : targetClass.getDeclaredFields())
                if (field.isAnnotationPresent(Column.class)) fields.add(field);
            columnsCache.put(targetClass.getName().hashCode(), fields);
        }
        return columnsCache.get(targetClass.getName().hashCode());
    }

    public static final <E extends ValueObject> List<Field> metadataFields(final Class<E> targetClass) {
        if (null == metadatasCache.get(targetClass.getName().hashCode())) {
            final List<Field> fields = new LinkedList<Field>();
            for (final Field field : targetClass.getDeclaredFields())
                if (field.isAnnotationPresent(Metadata.class)) fields.add(field);
            metadatasCache.put(targetClass.getName().hashCode(), fields);
        }
        return metadatasCache.get(targetClass.getName().hashCode());
    }
}
