package com.example.tacademy.fbtest189.Service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * 메세지 수신 : FCM 메시지가 도착하면 자동 호출된다.
 * 메세지 우선순위가 high가 아니면 안드6.0이상에서는 백그라운드시 즉각적으로 도달하지 않을 수 있다.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    //메시지를 수신한다.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {        //ctrl + i = implement, ctrl + o = override
        super.onMessageReceived(remoteMessage);

        Log.d("FCM", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("FCM", "Message data payload: " + remoteMessage.getData());
        }
    }
}
