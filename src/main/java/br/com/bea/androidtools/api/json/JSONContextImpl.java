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

package br.com.bea.androidtools.api.json;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import br.com.bea.androidtools.api.annotations.Metadata;
import br.com.bea.androidtools.api.metadata.MetadataObject;
import br.com.bea.androidtools.api.model.EntityUtils;
import br.com.bea.androidtools.api.model.ValueObject;

public class JSONContextImpl<E extends ValueObject> implements JSONContext<E> {

    private final List<MetadataObject> metadata;

    private final Class<E> targetClass;

    public JSONContextImpl(final Class<E> targetClass) {
        this.targetClass = targetClass;
        metadata = new LinkedList<MetadataObject>();
        for (final Field field : EntityUtils.metadataFields(targetClass))
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
                object.put(field.getAnnotation(Metadata.class).value(), field.get(vo));
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
