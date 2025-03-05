package com.service_demo_android.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class MyBackgroundService extends Service {

    // Context reference to the service
    public Context context = this;

    // Handler to post delayed tasks
    public Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        // Return null since this is a started service, not a bound service
        return null;
    }

    @Override
    public void onCreate() {
        // Show a Toast when the service is created
        Toast.makeText(this, "Background Service created!", Toast.LENGTH_LONG).show();

        // Post a delayed task to show a Toast message every 5 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Show a Toast indicating that the service is still running
                Toast.makeText(context, "Background Service is still running", Toast.LENGTH_LONG).show();

                // Repeat the task every 5 seconds
                handler.postDelayed(this, 5000); // Repeat every 5 seconds
            }
        }, 0); // Start immediately
    }

    @Override
    public void onDestroy() {
        // Show a Toast when the service is destroyed
        Toast.makeText(this, "Background Service stopped", Toast.LENGTH_LONG).show();

        // Clean up and stop the repeated task when the service is destroyed
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onStart(Intent intent, int startid) {
        // Show a Toast when the service is started
        Toast.makeText(this, "Background Service started", Toast.LENGTH_LONG).show();
    }
}
