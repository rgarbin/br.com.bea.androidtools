package br.com.bea.androidtools.api.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class AndroidUtils {

    public static final boolean hasConnectivity(final Context context, final int networkType) {
        final ConnectivityManager connectivity = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ConnectivityManager.isNetworkTypeValid(networkType)) {
            final NetworkInfo info = connectivity.getNetworkInfo(networkType);
            return info.isAvailable() || info.isConnected() || info.isConnectedOrConnecting();
        }
        return false;
    }
}
