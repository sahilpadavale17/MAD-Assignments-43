package com.example.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "advanced_notification_channel";
    private static final int PERMISSION_REQUEST_CODE = 101;
    
    // Different IDs for different notifications
    private static final int SIMPLE_ID = 1;
    private static final int COLORED_ID = 2;
    private static final int BIG_TEXT_ID = 3;
    private static final int ACTION_ID = 4;

    private int pendingNotificationType = 0; // 1: Simple, 2: Colored, 3: BigText, 4: Action

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createNotificationChannel();

        findViewById(R.id.btnSimpleNotify).setOnClickListener(v -> handleNotificationClick(1));
        findViewById(R.id.btnColoredNotify).setOnClickListener(v -> handleNotificationClick(2));
        findViewById(R.id.btnBigTextNotify).setOnClickListener(v -> handleNotificationClick(3));
        findViewById(R.id.btnActionNotify).setOnClickListener(v -> handleNotificationClick(4));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Demo Channel";
            String description = "Channel for various notification types";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void handleNotificationClick(int type) {
        pendingNotificationType = type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            } else {
                showSelectedNotification();
            }
        } else {
            showSelectedNotification();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSelectedNotification();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showSelectedNotification() {
        switch (pendingNotificationType) {
            case 1: showSimpleNotification(); break;
            case 2: showColoredNotification(); break;
            case 3: showBigTextNotification(); break;
            case 4: showActionNotification(); break;
        }
    }

    private void showSimpleNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Simple Notification")
                .setContentText("This is a basic notification.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notifyApp(SIMPLE_ID, builder);
    }

    private void showColoredNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Colored Notification")
                .setContentText("This notification has a custom background color!")
                .setColor(Color.BLUE) // Sets the accent color
                .setColorized(true)   // Makes the whole notification background colored (may vary by Android version)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notifyApp(COLORED_ID, builder);
    }

    private void showBigTextNotification() {
        String longText = "This is a much longer text that wouldn't fit in a single line. " +
                "By using BigTextStyle, we can show a lot more content to the user in the expanded view of the notification.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_edit)
                .setContentTitle("Big Text Style")
                .setContentText("Expand to see more...")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(longText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notifyApp(BIG_TEXT_ID, builder);
    }

    private void showActionNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Action button intent
        Intent actionIntent = new Intent(this, MainActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 1, actionIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_send)
                .setContentTitle("Action Notification")
                .setContentText("Click the button below!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_menu_view, "Open App", actionPendingIntent)
                .setAutoCancel(true);

        notifyApp(ACTION_ID, builder);
    }

    private void notifyApp(int id, NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            notificationManager.notify(id, builder.build());
        }
    }
}
