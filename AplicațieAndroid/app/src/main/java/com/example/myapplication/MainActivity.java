package com.example.myapplication;

import custom.BluetoothManager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.*;
import android.view.View;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    /*HandlerThread backgroundThread;
    Handler backgroundHandler;
    Runnable backgroundRunnable = new Runnable(){
        @Override
        public void run() {
            if(bt.checkConnection()){
                connectionLabel.setText(R.string.connected);
            }else{
                connectionLabel.setText(R.string.disconnected);
            }
        }
    };*/

    private BluetoothManager bt;
    private Button forward, reverse, left, right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*backgroundThread = new HandlerThread("backgroundThread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());*/


        bt = new BluetoothManager(this);
        //backgroundHandler.post(backgroundRunnable);
        bt.startReceiving();

        forward = findViewById(R.id.Accelerate);
        reverse = findViewById(R.id.Reverse);
        left = findViewById(R.id.Left);
        right = findViewById(R.id.Right);

        forward.setOnTouchListener(this::Forward_Touch);
        reverse.setOnTouchListener(this::Reverse_Touch);
        left.setOnTouchListener(this::Left_Touch);
        right.setOnTouchListener(this::Right_Touch);
    }

    boolean forwardButtonPressed = false;
    boolean reverseButtonPressed = false;
    boolean leftButtonPressed = false;
    boolean rightButtonPressed = false;

    boolean Forward_Touch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                forwardButtonPressed = true;
                if (bt.checkConnection()) {
                    bt.Send("forward");
                } else {
                    Toast.makeText(this, "No device connected through bluetooth", Toast.LENGTH_SHORT).show();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (forwardButtonPressed) {
                    forwardButtonPressed = false;
                    if (bt.checkConnection()) {
                        bt.Send("stop");
                        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;//SA FAC ASTA SI LA RESTUL BUTOANELOR*/
            
            case MotionEvent.ACTION_CANCEL:
                if (forwardButtonPressed) {
                    forwardButtonPressed = false;
                    Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    if (bt.checkConnection()) {
                        bt.Send("stop");
                        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
        }
        return false;
    }

    boolean Reverse_Touch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                reverseButtonPressed = true;
                if (bt.checkConnection()) {
                    bt.Send("reverse");
                } else {
                    Toast.makeText(this, "No device connected through bluetooth", Toast.LENGTH_SHORT).show();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (reverseButtonPressed) {
                    reverseButtonPressed = false;
                    if (bt.checkConnection()) {
                        bt.Send("stop");
                        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;//SA FAC ASTA SI LA RESTUL BUTOANELOR*/

            case MotionEvent.ACTION_CANCEL:
                if (reverseButtonPressed) {
                    reverseButtonPressed = false;
                    Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    if (bt.checkConnection()) {
                        bt.Send("stop");
                        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
        }
        return false;
    }

    boolean Left_Touch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                leftButtonPressed = true;
                if (bt.checkConnection()) {
                    bt.Send("left");
                } else {
                    Toast.makeText(this, "No device connected through bluetooth", Toast.LENGTH_SHORT).show();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (leftButtonPressed) {
                    leftButtonPressed = false;
                    if (bt.checkConnection()) {
                        bt.Send("stopSteering");
                        //Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;

            case MotionEvent.ACTION_CANCEL:
                if (leftButtonPressed) {
                    leftButtonPressed = false;
                    if (bt.checkConnection()) {
                        bt.Send("stopSteering");
                        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
        }
        return false;
    }

    boolean Right_Touch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rightButtonPressed = true;
                if (bt.checkConnection()) {
                    bt.Send("right");
                } else {
                    Toast.makeText(this, "No device connected through bluetooth", Toast.LENGTH_SHORT).show();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (rightButtonPressed) {
                    rightButtonPressed = false;
                    if (bt.checkConnection()) {
                        bt.Send("stopSteering");
                        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;

            case MotionEvent.ACTION_CANCEL:
                if (rightButtonPressed) {
                    rightButtonPressed = false;
                    if (bt.checkConnection()) {
                        bt.Send("stopSteering");
                        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                Log.d("acces", "Permissions granted");
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
                bt.setPermissionsGranted(true);  // Proceed with Bluetooth connection after permissions are granted
            } else{
                bt.setPermissionsGranted(false);
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle Bluetooth enabling result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) { // Bluetooth enable request
            if (resultCode == RESULT_OK) {
               // bt.connectToEsp32();  // Proceed with Bluetooth connection after enabling Bluetooth
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
