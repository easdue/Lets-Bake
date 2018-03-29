package nl.erikduisters.letsbake.data.local;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.di.ApplicationContext;

/**
 * Created by Erik Duisters on 26-03-2018.
 */

@Singleton
public class NetworkMonitor {
    private final Context context;
    private final ConnectivityManager connectivityManager;
    private final IntentFilter connectivityIntentFilter;
    private final ConnectivityBroadcastReceiver connectivityBroadcastReceiver;
    private final List<NetworkConnectedListener> networkConnectedListeners;
    private final Object networkConnectedListernersLock;

    @Inject
    NetworkMonitor(@ApplicationContext Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        connectivityIntentFilter = new IntentFilter();
        connectivityIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        connectivityBroadcastReceiver = new ConnectivityBroadcastReceiver();

        networkConnectedListeners = new ArrayList<>();
        networkConnectedListernersLock = new Object();
    }

    public boolean isNetworkConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public void addListener(NetworkConnectedListener listener) {
        synchronized (networkConnectedListernersLock) {
            if (networkConnectedListeners.contains(listener)) {
                return;
            }

            if (networkConnectedListeners.size() == 0) {
                context.registerReceiver(connectivityBroadcastReceiver, connectivityIntentFilter);
            }

            networkConnectedListeners.add(listener);
        }
    }

    public synchronized void removeListener(NetworkConnectedListener listener) {
        synchronized (networkConnectedListernersLock) {
            if (networkConnectedListeners.contains(listener)) {
                networkConnectedListeners.remove(listener);

                if (networkConnectedListeners.size() == 0) {
                    unregisterReceiver();
                }
            }
        }
    }

    private void unregisterReceiver() {
        context.unregisterReceiver(connectivityBroadcastReceiver);
    }

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isNetworkConnected()) {
                synchronized (networkConnectedListernersLock) {
                    for (NetworkConnectedListener listener : networkConnectedListeners) {
                        listener.onNetworkConnected();
                    }

                    networkConnectedListeners.clear();

                    unregisterReceiver();
                }
            }
        }
    }

    public interface NetworkConnectedListener {
        void onNetworkConnected();
    }
}
