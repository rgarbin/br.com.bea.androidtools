package br.com.bea.androidtools.api.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class AndroidUtils {

    public static final boolean hasConnectivity(final Context context, final int... networkTypes) {
        final ConnectivityManager connectivity = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (networkTypes.length == 0) for (final NetworkInfo info : connectivity.getAllNetworkInfo())
            if (info.isAvailable() || info.isConnected() || info.isConnectedOrConnecting()) return true;
        for (final int networkType : networkTypes)
            if (ConnectivityManager.isNetworkTypeValid(networkType)) {
                final NetworkInfo info = connectivity.getNetworkInfo(networkType);
                if (info.isAvailable() || info.isConnected() || info.isConnectedOrConnecting()) return true;
            }
        return false;
    }
}
