package com.example.psalms;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationsActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationManager = NotificationManagerCompat.from(this);

        Button addBtn = (Button)findViewById(R.id.addBtn);

        Button testBtn = (Button)findViewById(R.id.testBtn);
        final Context context = this;
        testBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                notificationManager.notify(1, new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.cross)
                        .setContentTitle("Psalm Test Notification")
                        .setContentText("Testing")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build());
            }
        });
    }

    public void addNotification(View view) {
//            LayoutInflater inflater = (LayoutInflater)
//                    getSystemService(LAYOUT_INFLATER_SERVICE);
//            View popupView = inflater.inflate(R.layout.activity_clock, null);
//
//            // create the popup window
//            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//            boolean focusable = true; // lets taps outside the popup also dismiss it
//            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//            // show the popup window
//            // which view you pass in doesn't matter, it is only used for the window tolken
//            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//            // dismiss the popup window when touched
//            popupView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popupWindow.dismiss();
//                    return true;
//                }
//            });
    }

    public void testNotification(View view){
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.cross)
                .setContentTitle("Psalm Test Notification")
                .setContentText("Testing")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }
}
