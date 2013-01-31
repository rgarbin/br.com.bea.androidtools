package br.com.bea.androidtools.json;

import java.util.List;
import org.json.JSONArray;
import br.com.bea.androidtools.model.ValueObject;

public interface Parser<E extends ValueObject> {
    List<E> parse(final JSONArray value);
}
