package br.com.bea.androidtools.api.service;

import java.util.List;
import br.com.bea.androidtools.api.model.ValueObject;

public interface Service<E extends ValueObject> {
    E find(E vo);

    List<E> search(E vo);
}
