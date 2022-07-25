package com.posco.samplepush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FMS";


    NotificationManager manager;

    private static String CHANNEL_ID = "channel";
    private static String CHANNEL_NAME = "Channel";

    //if문을 사용하여 null을 처리할 것.

    //APP 메세지
    String from = null;
    String contents = null;

    //FMS 메세지
    String title = null;
    String body = null;



    public MyFirebaseMessagingService() {
    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived 호출됨: " + remoteMessage);



        //앱에서 보내는 메세지.
        Map<String, String> data = remoteMessage.getData();
        contents = data.get("contents");
        //앱에서 보낸 메세지에 대한 로그.
        Log.d(TAG, (contents==null)+"121313");//true:웹에서, false:앱에서


        if(contents==null){

            //FMS메세지.
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();

        }else {

            //어디서 오는 메세지인지 표시.
            from = remoteMessage.getFrom();
            body = contents;
        }



        Log.d(TAG, "from" + from + ",contents" + contents);
        sendToActivity(getApplicationContext(), from, contents); //알림 클릭시, 해당 앱으로 이동 / 필요



        //알람 호출!
        showNoti2();


    }


    public void showNoti2() {

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            ));

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(android.R.drawable.ic_menu_view);


        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        Notification noti = builder.build();

        manager.notify(2, noti);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("많은 글자들입니다");
        style.setBigContentTitle("제목입니다");
        style.setSummaryText("요약 글입니다");

        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, "channel3")
                .setContentTitle("알림 제목")
                .setContentText("알림 내용")
                .setSmallIcon(android.R.drawable.ic_menu_send)
                .setStyle(style);

    }


    private void sendToActivity(Context context, String from, String contents) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("contents", contents);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }

}