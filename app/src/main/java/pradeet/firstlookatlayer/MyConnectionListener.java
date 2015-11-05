package pradeet.firstlookatlayer;

import android.util.Log;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerConnectionListener;

public class MyConnectionListener implements LayerConnectionListener {

    private static final String TAG = MyConnectionListener.class.getSimpleName();

    private MainActivity main_activity;

    public MyConnectionListener(MainActivity ma) {
        main_activity = ma;
    }

    public void onConnectionConnected(LayerClient client) {
        if (client.isAuthenticated())
            main_activity.onUserAuthenticated();
        else
            client.authenticate();

    }
    public void onConnectionDisconnected(LayerClient client) {
        Log.v(TAG, "Connection to Layer closed");
    }

    public void onConnectionError(LayerClient client, LayerException e) {
        Log.v(TAG, "Error connecting to layer: " + e.toString());
    }
}