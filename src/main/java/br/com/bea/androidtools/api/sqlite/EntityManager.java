package br.com.bea.androidtools.api.sqlite;

import java.util.List;
import android.content.Context;
import br.com.bea.androidtools.api.model.Entity;

public interface EntityManager {

    void close();

    <E extends Entity<?>> void delete(E entity);

    <E extends Entity<?>> E find(E entity);

    <E extends Entity<?>> EntityManager init(Context context, String database, List<Class<E>> targetClasses);

    <E extends Entity<?>> E persist(E entity);

    <E extends Entity<?>> List<E> search(final QueryBuilder query);

    <E extends Entity<?>> E update(E entity);

}
