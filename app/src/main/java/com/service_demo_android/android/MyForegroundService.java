package com.service_demo_android.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.concurrent.atomic.AtomicInteger;

public class MyForegroundService extends Service {
    private static final String CHANNEL_ID = "ForegroundServiceChannel"; // Make sure this is consistent
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = MyForegroundService.class.getSimpleName(); // Use a constant for the tag
    private final Handler handler = new Handler();
    private final AtomicInteger counter = new AtomicInteger(0); // Thread-safe counter
    private NotificationManager manager;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the NotificationManager
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android 8.0 (API level 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            // Ensure the notification manager is properly initialized
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }

        // Create the first notification for the foreground service
        Notification notification = createNotification();

        // Start the service in the foreground
        startForeground(NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Not used in this example
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Handle commands sent to the service (if any)
        Toast.makeText(this, "Foreground Service Started", Toast.LENGTH_SHORT).show();

        // Use handler to repeatedly update the notification
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "=> Foreground Service is running...");
                int updatedCounter = counter.incrementAndGet(); // Thread-safe increment
                Notification notification = createNotification();
                if (manager != null) {
                    manager.notify(NOTIFICATION_ID, notification); // Update notification
                }

                // Keep the task running by calling it again after a delay
                handler.postDelayed(this, 5000); // Repeat every 5 seconds
            }
        }, 0); // Start immediately

        // Returning START_STICKY so the service will restart if it's killed
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Foreground Service stopped", Toast.LENGTH_SHORT).show();
        handler.removeCallbacksAndMessages(null); // Clean up to stop the repeated task
        Log.e(TAG, "Foreground Service destroyed.");
    }

    // Helper method to create a notification with the current counter value
    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID) // Make sure to use the channel ID here
                .setContentTitle("Foreground Service Running")
                .setContentText("This service is running. Counter: " + counter.get())
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .build();
    }
}
