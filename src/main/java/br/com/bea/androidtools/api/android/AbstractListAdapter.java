package br.com.bea.androidtools.api.android;

import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import br.com.bea.androidtools.api.model.Entity;
import br.com.bea.androidtools.api.model.ValueObject;

public abstract class AbstractListAdapter<E extends ValueObject> extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<E> list = new ArrayList<E>(0);

    public AbstractListAdapter(final LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void add(final E element) {
        this.list.add(element);
    }

    public void addAll(final List<E> list) {
        this.list.addAll(list);
    }

    public void clear() {
        list.clear();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(final int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(final int position) {
        final E e = list.get(position);
        return e instanceof Entity ? ((Integer) ((Entity) e).getId()).longValue() : position;
    }
}
