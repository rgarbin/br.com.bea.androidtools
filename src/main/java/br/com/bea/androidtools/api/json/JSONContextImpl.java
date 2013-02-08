package br.com.bea.androidtools.api.json;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import br.com.bea.androidtools.api.annotations.Metadata;
import br.com.bea.androidtools.api.metadata.MetadataObject;
import br.com.bea.androidtools.api.model.ValueObject;

public class JSONContextImpl<E extends ValueObject> implements JSONContext<E> {

    private final List<MetadataObject> metadata;

    private final Class<E> targetClass;

    public JSONContextImpl(final Class<E> targetClass) {
        this.targetClass = targetClass;
        metadata = new LinkedList<MetadataObject>();
        for (final Field field : targetClass.getDeclaredFields())
            if (field.isAnnotationPresent(Metadata.class))
                metadata.add(new MetadataObject(field.getName(), field.getAnnotation(Metadata.class).value()));
    }

    public JSONContextImpl(final List<MetadataObject> metadata, final Class<E> targetClass) {
        this.metadata = metadata;
        this.targetClass = targetClass;
    }

    @Override
    public JSONObject marshal(final E vo) {
        final JSONObject object = new JSONObject();
        try {
            for (final MetadataObject mdo : metadata) {
                final Field field = targetClass.getDeclaredField(mdo.getFieldName());
                field.setAccessible(true);
                object.put(mdo.getValue(), field.get(vo));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public List<E> unmarshal(final JSONArray value) {
        final List<E> result = new LinkedList<E>();
        try {
            for (int i = 0; i < value.length(); i++) {
                final JSONObject object = (JSONObject) value.get(i);
                final E vo = targetClass.newInstance();
                for (final MetadataObject mdo : metadata) {
                    final Field field = targetClass.getDeclaredField(mdo.getFieldName());
                    field.setAccessible(true);
                    field.set(vo, object.get(mdo.getValue()));
                }
                result.add(vo);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
