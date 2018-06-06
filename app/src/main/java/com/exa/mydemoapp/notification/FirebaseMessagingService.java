package com.exa.mydemoapp.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.Common.Constants;
import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.NotificationModel;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Logger;


/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String strPayload = remoteMessage.getData().get("payload");

        if (!TextUtils.isEmpty(strPayload)) {
            sendNotification(strPayload);
        }

        /*if(msgType.equals(IConstants.MSG_TYPE_ALL)){
            showNotification(msg,title);
        }
        if(msgType.equals(IConstants.MSG_TYPE_PERSONAL)&&imei.equals(myImei)){*/

        //}


        /*  String clientEmail=remoteMessage.getData().get("useremail");
        SharedPreferences prefs = this.getSharedPreferences("MY_CHAT_PREFS", this.MODE_PRIVATE);
        String userId = prefs.getString("userEmail","");
        if(userId.equalsIgnoreCase(clientEmail)) {*/

        //}
    }

    private void sendNotification(String strPayload) {
        String strNotificationType = "";

        Gson gson = new Gson();
        NotificationModel notificationModel = gson.fromJson(strPayload, NotificationModel.class);

        int iNotificationCount = CommonUtils.getSharedPref(this, "NOTIF_COUNT");
        iNotificationCount = iNotificationCount + 1;
        CommonUtils.InsertSharedPref(this, "NOTIF_COUNT", iNotificationCount);
        Intent intent = null;

        if (CommonUtils.getSharedPref(Constants.USER_NAME, this) != null && !CommonUtils.getSharedPref(Constants.USER_NAME, this).isEmpty()) {

   /*         if (notificationModel != null) {
                strNotificationType = notificationModel.getType();
                if (!TextUtils.isEmpty(strNotificationType)) {
                    if (strNotificationType.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_NEW_REQUEST)) {
                        intent = new Intent(this, TechnicianHomeActivity.class);
                        intent.putExtra(AppConstants.NOTIFICATION_TYPE_NEW_REQUEST, true);
                    } else if (strNotificationType.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_ADMIN_ASSIGN_SERVICE_REQUEST)) {
                        intent = new Intent(this, TechnicianHomeActivity.class);
                        if (notificationModel != null) {
                            intent.putExtra(AppConstants.SCHEDULED_DATE, notificationModel.getScheduledTime());
                        }
                        intent.putExtra(AppConstants.NOTIFICATION_TYPE_ADMIN_ASSIGN_SERVICE_REQUEST, true);
                    } else if (strNotificationType.equalsIgnoreCase(AppConstants.NOTIFCATION_TYPE_ADMIN_RESCHEDULE_SERVICE_REQUEST)) {
                        intent = new Intent(this, TechnicianHomeActivity.class);
                        if (notificationModel != null) {
                            intent.putExtra(AppConstants.SCHEDULED_DATE, notificationModel.getScheduledTime());
                        }
                        intent.putExtra(AppConstants.NOTIFCATION_TYPE_ADMIN_RESCHEDULE_SERVICE_REQUEST, true);
                    } else if (strNotificationType.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_CUSTOMER_RESCHEDULE_SERVICE_REQUEST)) {
                        intent = new Intent(this, TechnicianHomeActivity.class);
                        if (notificationModel != null) {
                            intent.putExtra(AppConstants.SCHEDULED_DATE, notificationModel.getScheduledTime());
                        }
                        intent.putExtra(AppConstants.NOTIFICATION_TYPE_CUSTOMER_RESCHEDULE_SERVICE_REQUEST, true);
                    } else if (strNotificationType.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_SERVICE_REQUEST_CANCEL_BY_ADMIN)) {
                        intent = new Intent();
                        intent.putExtra(AppConstants.NOTIFICATION_TYPE_SERVICE_REQUEST_CANCEL_BY_ADMIN, true);
                    } else if (strNotificationType.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_SERVICE_REQUEST_CANCEL_BY_CUSTOMER)) {
                        intent = new Intent();
                        intent.putExtra(AppConstants.NOTIFICATION_TYPE_SERVICE_REQUEST_CANCEL_BY_CUSTOMER, true);
                    } else if (strNotificationType.equalsIgnoreCase(AppConstants.NOTIFICATION_TYPE_PAYMENT_MADE_BY_CUSTOMER)) {
                        intent = new Intent();
                        intent.putExtra(AppConstants.NOTIFICATION_TYPE_PAYMENT_MADE_BY_CUSTOMER, true);
                    }
                }
            }
        } else {*/
            intent = new Intent(this, HomeActivity.class);
            //}
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //  intent.putExtra(AppConstants.NOTIFICATION_TYPE, strNotificationType);
            } else {
                return;
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            if (notificationModel != null) {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setLargeIcon(bm)
                        .setContentTitle(notificationModel.getTitle() != null ? notificationModel.getTitle() : "")
                        .setContentText(notificationModel.getBody() != null ? notificationModel.getBody() : "")
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationModel.getTitle() != null ? notificationModel.getTitle() : ""))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationModel.getBody() != null ? notificationModel.getBody() : ""))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(iNotificationCount, notificationBuilder.build());
            } else {
                return;
            }
        }


    }
}
