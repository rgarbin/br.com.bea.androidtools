package br.com.bea.androidtools.api.json;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import br.com.bea.androidtools.api.model.ValueObject;

public interface JSONContext<E extends ValueObject> {
    JSONObject marshal(final E vo);

    List<E> unmarshal(final JSONArray value);
}
