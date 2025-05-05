package custom;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.bluetooth.*;
import android.widget.TextView;
import android.widget.Toast;

import android.os.HandlerThread;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import java.io.*;
import java.util.UUID;
import android.util.Log;

import com.example.myapplication.R;

public class BluetoothManager {

    HandlerThread ConnectionThread;
    Handler ConnectionHandler;

    HandlerThread ReceiverThread;
    Handler ReceiverHandler;

    private final Activity activity;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice bluetoothDevice;
    private InputStream inputStream;
    private OutputStream outputStream;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String esp32_name = "ESP-32";

    private final int PERMISSION_REQUEST_CODE = 1;
    private final int BLUETOOTH_ENABLE_REQUEST = 2;

    private boolean PermissionsGranted = false; // Explicit initialization

    TextView connectionLabel;

    public BluetoothManager(Activity activity) {
        this.activity = activity;
        this.connectionLabel = activity.findViewById(R.id.connectionLabel); // Initialize here

        ConnectionThread = new HandlerThread("ConnectionThread");
        ConnectionThread.start();
        ConnectionHandler = new Handler(ConnectionThread.getLooper());

        ReceiverThread = new HandlerThread("ReceiverThread");
        ReceiverThread.start();
        ReceiverHandler = new Handler(ReceiverThread.getLooper());

        checkPermissions();
    }

    public void setPermissionsGranted(boolean value) {
        PermissionsGranted = value;
        if(value) {
            EnableBluetooth();
        }else{
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectionLabel.setText(R.string.permissionsDeclined);
                }
            });
        }
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN},
                    PERMISSION_REQUEST_CODE);
        } else {
            PermissionsGranted = true;
            Toast.makeText(activity, "Bluetooth Permissions Already Granted", Toast.LENGTH_SHORT).show();
            EnableBluetooth(); // Enable bluetooth right away.
        }
    }

    public void EnableBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Log.e("Bluetooth", "Bluetooth not supported on this device");
            return;
        }

        // Check if Bluetooth is enabled
        if(!bluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, BLUETOOTH_ENABLE_REQUEST); // Request Bluetooth enable
        }

        if(bluetoothAdapter.isEnabled()){
            searchEsp32();
        }

    }

    private void searchEsp32() {
        ConnectionHandler.post(new Runnable() {
            @Override
            public void run() {
                // First, try to find the ESP32 from paired devices
                bluetoothDevice = getDevice(esp32_name);

                if (bluetoothDevice == null) {
                    // If the device is not found in paired devices, retry after a short delay
                    Log.d("BluetoothSearch", "ESP32 Device not found in paired devices, retrying...");
                    ConnectionHandler.postDelayed(this, 100);  // Retry after 3 seconds
                } else {
                    // Bluetooth device found, create connection
                    connectToDevice(bluetoothDevice);
                }
            }
        });
    }

    private void connectToDevice(BluetoothDevice device) {
        ConnectionHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    bluetoothSocket.connect();  // Attempt to connect

                    inputStream = bluetoothSocket.getInputStream();
                    outputStream = bluetoothSocket.getOutputStream();

                    Log.d("BluetoothSearch", "Connected to ESP32");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            connectionLabel.setText(R.string.connected);
                            Toast.makeText(activity, "Connected to your ESP32", Toast.LENGTH_SHORT).show();
                        }
                    });

                    startReceiving();

                } catch (IOException e) {
                    Log.e("Bluetooth", "Error connecting or communicating with ESP32", e);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            connectionLabel.setText(R.string.disconnected);
                        }
                    });
                    // Retry if connection failed
                    Log.d("BluetoothConnection", "Retrying connection in 0.1 seconds...");
                    ConnectionHandler.postDelayed(this, 100);  // Retry after 1 seconds
                }
            }
        });
    }
    // Helper method to get Bluetooth device by name
    private BluetoothDevice getDevice(final String deviceName) {
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            if (device.getName().equals(deviceName)) {
                return device;
            }
        }
        return null;
    }

    //Send
    public void Send(String messageToSend) {
        if(checkConnection()){
            try{
                outputStream.write((messageToSend+"\n").getBytes());
                Log.d("Bluetooth", "Sent: " + messageToSend);
            }catch (IOException e) {
                Log.e("BluetoothSend", "Error sending to ESP32", e);
            }
        }else{
            Toast.makeText(activity, esp32_name + " not connected", Toast.LENGTH_SHORT).show();
        }

    }

    //Recieve
    public void processReceived(String receivedData) {
        // Update UI if needed (use runOnUiThread)
        activity.runOnUiThread(() -> {
            Toast.makeText(activity, "Received: " + receivedData, Toast.LENGTH_SHORT).show();

        });
    }

    public void startReceiving() {
        if (inputStream == null) {
            Log.e("Bluetooth", "InputStream is null. Cannot start receive thread.");
            return;
        }

        ReceiverHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] buffer = new byte[1024];
                    int bytes;
                    while (checkConnection() && (bytes = inputStream.read(buffer)) != -1) {
                        String receivedData = new String(buffer, 0, bytes);
                        Log.d("BluetoothDebug", "Received: " + receivedData);
                        processReceived(receivedData);
                    }
                } catch (IOException e) {
                    Log.e("Bluetooth", "Error receiving data", e);
                    searchEsp32(); // Attempt to reconnect if stream is broken
                }
            }
        });
    }

    public boolean checkConnection(){
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }

    public void Destroy() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            Log.e("BluetoothManager", "Error closing resources", e);
        }
    }
}
