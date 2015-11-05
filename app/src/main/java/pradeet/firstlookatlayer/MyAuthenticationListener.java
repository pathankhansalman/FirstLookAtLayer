package pradeet.firstlookatlayer;

import android.os.AsyncTask;
import android.util.Log;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerAuthenticationListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class MyAuthenticationListener implements LayerAuthenticationListener {

    private static final String TAG = MyAuthenticationListener.class.getSimpleName();

    private MainActivity main_activity;

    public MyAuthenticationListener(MainActivity ma) {
        main_activity = ma;
    }

    public void onAuthenticationChallenge(final LayerClient client, final String nonce) {
        final String mUserId = MainActivity.getUserID();

        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    HttpPost post = new HttpPost("https://layer-identity-provider.herokuapp" + ".com/identity_tokens");
                    post.setHeader("Content-Type", "application/json");
                    post.setHeader("Accept", "application/json");

                    JSONObject json = new JSONObject()
                            .put("app_id", client.getAppId())
                            .put("user_id", mUserId)
                            .put("nonce", nonce);
                    post.setEntity(new StringEntity(json.toString()));

                    HttpResponse response = (new DefaultHttpClient()).execute(post);
                    String eit = (new JSONObject(EntityUtils.toString(response.getEntity())))
                            .optString("identity_token");
                    Log.d("Debug....",eit);
                    client.answerAuthenticationChallenge(eit);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).execute();
    }

    public void onAuthenticated(LayerClient client, String userID) {

        if (main_activity != null)
            main_activity.onUserAuthenticated();
    }

    public void onAuthenticationError(LayerClient layerClient, LayerException e) {

    }

    public void onDeauthenticated(LayerClient client) {

    }
}