package com.service_demo_android.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declare a reference to the bound service and a flag to track the binding state
    private MyBoundService boundService;
    private boolean isBound = false;

    // ServiceConnection to manage the binding and unbinding of the bound service
    private final ServiceConnection serviceConnection = new ServiceConnection() {

        // This method is called when the service is successfully connected
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Get the binder from the service to allow interaction with it
            MyBoundService.LocalBinder binder = (MyBoundService.LocalBinder) service;
            boundService = binder.getService();
            isBound = true;  // Set the bound flag to true
        }

        // This method is called when the service is unexpectedly disconnected
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;  // Reset the bound flag when service is disconnected
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge support for modern display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);  // Set the layout for the activity

        // Set up an onClickListener for the "Start Foreground Service" button
        findViewById(R.id.btn_start_foreground).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyForegroundService.class);
            // Start foreground service, handle compatibility for Android O and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        });

        // Set up an onClickListener for the "Stop Foreground Service" button
        findViewById(R.id.btn_stop_foreground).setOnClickListener(v -> {
            // Stop the foreground service
            stopService(new Intent(MainActivity.this, MyForegroundService.class));
        });

        // Set up an onClickListener for the "Start Background Service" button
        findViewById(R.id.btn_start_background).setOnClickListener(v -> {
            // Start the background service
            startService(new Intent(this, MyBackgroundService.class));
        });

        // Set up an onClickListener for the "Stop Background Service" button
        findViewById(R.id.btn_stop_background).setOnClickListener(v -> {
            // Stop the background service
            stopService(new Intent(this, MyBackgroundService.class));
        });

        // Set up an onClickListener for the "Bind to Bound Service" button
        findViewById(R.id.btn_bound).setOnClickListener(v -> {
            // Bind to the bound service when this button is clicked
            bindService(new Intent(MainActivity.this, MyBoundService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        });

        // Set up an onClickListener for the "Unbind from Bound Service" button
        findViewById(R.id.btn_unbound).setOnClickListener(v -> {
            if (isBound) {
                // Unbind from the service if currently bound
                unbindService(serviceConnection);
                isBound = false;  // Reset the bound flag
            }
        });

        // Set up an onClickListener for the "Show Bound Service Message" button
        findViewById(R.id.btn_show_bound_message).setOnClickListener(v -> {
            if (isBound) {
                // If the service is bound, show the message from the bound service
                String message = boundService.getServiceMessage();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
