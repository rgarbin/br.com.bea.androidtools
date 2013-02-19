package br.com.bea.androidtools.api.model;

import java.io.Serializable;
import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;

public abstract class ValueObject implements Parcelable, Serializable {
    private static final long serialVersionUID = 1L;
    private final Date version = new Date();

    @Override
    public int describeContents() {
        return hashCode();
    }

    public Date getVersion() {
        return version;
    }

    public Serializable readFromParcel(final Parcel parcel) {
        return parcel.readSerializable();
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int flags) {
        parcel.writeSerializable(this);
    }
}
