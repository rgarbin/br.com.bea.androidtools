package br.com.bea.androidtools.json;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import br.com.bea.androidtools.annotations.Metadata;
import br.com.bea.androidtools.metadata.MetadataObject;
import br.com.bea.androidtools.model.ValueObject;

public class ParserImpl<E extends ValueObject> implements Parser<E> {

    private final List<MetadataObject> metadata;

    private final Class<E> targetClass;

    public ParserImpl(final Class<E> targetClass) {
        this.targetClass = targetClass;
        metadata = new LinkedList<MetadataObject>();
        for (final Field field : targetClass.getDeclaredFields())
            if (field.isAnnotationPresent(Metadata.class))
                metadata.add(new MetadataObject(field.getName(), field.getAnnotation(Metadata.class).value()));
    }

    public ParserImpl(final List<MetadataObject> metadata, final Class<E> targetClass) {
        this.metadata = metadata;
        this.targetClass = targetClass;
    }

    @Override
    public List<E> parse(final JSONArray value) {
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
