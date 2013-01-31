package br.com.bea.androidtools.model;

import java.util.Date;
import android.os.Parcelable;

public abstract class ValueObject implements Parcelable {
    private final Date version = new Date();

    public Date getVersion() {
        return version;
    }
}
