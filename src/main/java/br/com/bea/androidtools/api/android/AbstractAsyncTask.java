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
        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(final Long... values) {
        super.onProgressUpdate(values);
    }

    public abstract void resultCallback(final List<E> result);

}
