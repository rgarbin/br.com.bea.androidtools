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

package br.com.bea.androidtools.api.android;

import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import br.com.bea.androidtools.api.model.Entity;
import br.com.bea.androidtools.api.model.ValueObject;

public abstract class AbstractListAdapter<E extends ValueObject> extends BaseAdapter {

    protected final LayoutInflater inflater;
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
    public E getItem(final int position) {
        return list.get(position);
    }

    @SuppressWarnings("unchecked")
    @Override
    public long getItemId(final int position) {
        final E e = list.get(position);
        return e instanceof Entity ? ((Entity<Number>) e).getId().longValue() : position;
    }
}
