package com.exa.mydemoapp.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;

import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //  String msgType=remoteMessage.getData().get("type");
        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        String msg = remoteMessage.getData().get("message");
        String title = remoteMessage.getData().get("title");

        /*if(msgType.equals(IConstants.MSG_TYPE_ALL)){
            showNotification(msg,title);
        }
        if(msgType.equals(IConstants.MSG_TYPE_PERSONAL)&&imei.equals(myImei)){*/
        showNotification(msg, title);
        //}


        /*  String clientEmail=remoteMessage.getData().get("useremail");
        SharedPreferences prefs = this.getSharedPreferences("MY_CHAT_PREFS", this.MODE_PRIVATE);
        String userId = prefs.getString("userEmail","");
        if(userId.equalsIgnoreCase(clientEmail)) {*/

        //}
    }

    private void showNotification(String message, String Title) {

        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(Title) // notification title
                .setContentText(message)
               // .setVibrate(new long[]{500, 500, 500, 500, 500})
                .setLights(Color.CYAN, 3000,
                        3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }


}
