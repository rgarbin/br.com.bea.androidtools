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

import java.util.List;
import android.os.AsyncTask;
import br.com.bea.androidtools.api.model.ValueObject;
import br.com.bea.androidtools.api.service.Service;

public abstract class AbstractAsyncTask<E extends ValueObject> extends AsyncTask<E, Long, List<E>> {

    private final Service<E> service;

    public AbstractAsyncTask(final Service<E> service) {
        this.service = service;
    }

    @Override
    protected List<E> doInBackground(final E... params) {
        return service.search(params.length > 0 ? params[0] : null);
    }

    @Override
    protected void onPostExecute(final List<E> result) {
        this.resultCallback(result);
    }

    @Override
    protected void onProgressUpdate(final Long... values) {
        super.onProgressUpdate(values);
    }

    public abstract void resultCallback(final List<E> result);

}
