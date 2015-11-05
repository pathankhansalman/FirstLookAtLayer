package pradeet.firstlookatlayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.layer.sdk.LayerClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LAYER_APP_ID = "layer:///apps/staging/09f34378-8157-11e5-972f-1ce402003f46";

    public static final String GCM_PROJECT_NUMBER = "00000";

    private LayerClient layerClient;
    private ConversationViewController conversationView;

    //Layer connection and authentication callback listeners
    private MyConnectionListener connectionListener;
    private MyAuthenticationListener authenticationListener;

    //onCreate is called on App Start
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If we haven't created a LayerClient, show the loading splash screen
        if(layerClient == null)
            setContentView(R.layout.activity_loading);


        //Create the callback listeners

        if(connectionListener == null)
            connectionListener = new MyConnectionListener(this);

        if(authenticationListener == null)
            authenticationListener = new MyAuthenticationListener(this);

        loadLayerClient();

        if(layerClient != null && conversationView != null)
            layerClient.registerTypingIndicator(conversationView);
    }

    protected void onPause(){
        super.onPause();
        if(layerClient != null && conversationView != null)
            layerClient.unregisterTypingIndicator(conversationView);
    }

    private void loadLayerClient(){

            if(layerClient == null){

                LayerClient.Options options = new LayerClient.Options();
                options.googleCloudMessagingSenderId(GCM_PROJECT_NUMBER);
                options.historicSyncPolicy(LayerClient.Options.HistoricSyncPolicy.ALL_MESSAGES);


                layerClient = LayerClient.newInstance(this, LAYER_APP_ID, options);
                layerClient.registerConnectionListener(connectionListener);
                layerClient.registerAuthenticationListener(authenticationListener);
            }

            if (!layerClient.isConnected()) {
                layerClient.connect();

            } else if (!layerClient.isAuthenticated()) {
                layerClient.authenticate();

            } else {
                onUserAuthenticated();
            }
    }

    public static String getUserID(){
        return "User1";
    }

    public static List<String> RegisteredUsers(){
        List<String> users = new ArrayList<>();
        users.add("User1");
        users.add("User2");
        users.add("User3");
        return users;
    }

    public void onUserAuthenticated(){

        if(conversationView == null) {

            conversationView = new ConversationViewController(this, layerClient);

            if (layerClient != null) {
                layerClient.registerTypingIndicator(conversationView);
            }
        }
    }
}
