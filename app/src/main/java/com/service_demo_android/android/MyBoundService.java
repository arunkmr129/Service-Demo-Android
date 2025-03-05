package com.service_demo_android.android;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyBoundService extends Service {

    // Binder object to interact with the service
    private final IBinder binder = new LocalBinder();

    // LocalBinder class allows the client (MainActivity) to get access to the service instance
    public class LocalBinder extends Binder {
        // This method is used by the client to get an instance of the service
        MyBoundService getService() {
            return MyBoundService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Return the binder to the client (MainActivity), allowing interaction with the service
        return binder;
    }

    // Method that the client can call to get a message from the service
    public String getServiceMessage() {
        return "Bound Service is running!";  // Return a simple message
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Show a Toast when the service is created
        Toast.makeText(this, "Bound Service Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Show a Toast when the service is destroyed
        Toast.makeText(this, "Bound Service Destroyed", Toast.LENGTH_SHORT).show();
    }
}
