package lootab.rememberapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

import lootab.rememberapp.R;

/**
 * Created by ichung-gi on 2016. 1. 14..
 */
public class NotiStopActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_stop);
        // 알림을 종료
        NotificationManager notiMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notiMgr.cancel(ListAddActivity.NOTI_ID);
    }
}
