package com.cube.sewingmachine;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class Bluetooth {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_AUTO_ENABLE_BT = 3;
    // Intent request code

    private static final String TAG = "Bluetooth";

    private BluetoothAdapter bluetoothAdapter;

    private Activity mActivity;
    private Handler mHandler;

    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private int mstate=0;
    private String mAddress ="";
    private static final int STATE_NONE = 0;
    private static final int STATE_LISTEN = 1;
    private static final int STATE_CONNECTING = 2;//연결중
    private static final int STATE_CONNECTED = 3;//연결됨

    private synchronized void setState(int state)
    {
        mstate = state;
    }
    public synchronized int getState()
    {
        return mstate;
    }


    private synchronized void setAddress(String address)
    {
        mAddress = address;
    }
    public synchronized String getAddress()
    {
        return mAddress;
    }

    public synchronized void start(){
        if(mConnectThread==null){}
        else {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mConnectedThread==null){}
        else {
            mConnectedThread.cancel();
            mConnectedThread=null;
        }
    }
    public synchronized void connect(BluetoothDevice device){
        if (mstate==STATE_CONNECTING)
        {
            Log.e("STATE","CONNECTING");
            if (mConnectedThread==null){
                Log.e("STATE!","??");
            }
            else {
                Log.e("STATE","CANCEL");
                mConnectedThread.cancel();
                mConnectedThread=null;
            }
        }
        if (mConnectedThread==null){
            Log.e("STATE","??");
//            scanDevice();
//            return;
        }
        else {
            Log.e("STATE","CANCEL");
            mConnectedThread.cancel();
            mConnectedThread=null;
        }

        Log.e("1","1");
        mConnectThread = new ConnectThread(device);
        Log.e("2","2");
        mConnectThread.start();
        Log.e("3","3");
        setState(STATE_CONNECTING);
    }
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {

        if (mConnectThread == null){}
        else {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread == null) {}
        else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }
    public synchronized void stop() {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_NONE);
    }

    public void write(byte[] out)
    {
        Log.e("??","write 함수 들어옴");

        ConnectedThread send;
        synchronized (this)
        {
            Log.e("??","synchronized 들어옴");
            if (mstate!=STATE_CONNECTED)return;
            send=mConnectedThread;
        }

        send.write(out);
        Log.e("??","send함");
    }

    private void connectionFailed() {
        setState(STATE_LISTEN);
        LoginActivity.isConnected=false;
    }
    private void connectionLost() {
        setState(STATE_LISTEN);
        LoginActivity.isConnected=false;
    }
    private class ConnectThread extends Thread {
        private final BluetoothSocket mSocket;
        private final BluetoothDevice mDevice;

        public ConnectThread(BluetoothDevice device) {
            Log.e("hi","connectThread");
            mDevice = device;
            BluetoothSocket tempSocket = null;
            try {
                tempSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e("hi","error");
                e.printStackTrace();
            }
            mSocket = tempSocket;
        }


        public void run(){
            //setName("ConnectThread");
            Log.e("hi","run");
            bluetoothAdapter.cancelDiscovery();
            try {
                mSocket.connect();
            } catch (IOException e) {
                Log.e("??","error");
                connectionFailed();
                e.printStackTrace();
                try {

                    mSocket.close();
//                    Toast.makeText(mActivity,"기기를 다시 선택해주세요.",Toast.LENGTH_SHORT).show();
                    scanDevice();
                    return;
//                    Log.e("socket","close");
                } catch (IOException ee) {
                    e.printStackTrace();
                    Log.e("socket","error");
                }
                Bluetooth.this.start();
                Log.e("start","걍 잘되잖아");
                return;
            }

            synchronized(Bluetooth.this){
                mConnectThread=null;
            }
            connected(mSocket,mDevice);
        }
        public void cancel()
        {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mSocket;
        private final InputStream mInput;
        private final OutputStream mOutput;

        public ConnectedThread(BluetoothSocket socket)
        {
            mSocket=socket;
            InputStream tempInput=null;
            OutputStream tempOutput=null;
            LoginActivity.isConnected=true;

            try {
                tempInput=socket.getInputStream();
                tempOutput=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mInput= tempInput;
            mOutput=tempOutput;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            //Arrays.fill(buffer, (byte)0x00);
            int bytes;

        //    byte[] buffer_b = new byte[1024];
        //    Arrays.fill(buffer_b, (byte)0x00);
        //    int position=0;

            while (true)
            {
                try {
                    bytes = mInput.read(buffer);
                    Log.e("Testt","MSGREaddddd");
                    mHandler.obtainMessage(4,bytes,-1,buffer).sendToTarget();


                } catch (IOException e) {
                    e.printStackTrace();
                    connectionLost();
                    break;
                }
            }
        }
        public void write(byte[] buffer)
        {
            try {
                mOutput.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void cancel(){
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public Bluetooth(Activity act, Handler h)
    {
        mActivity = act;
        mHandler = h;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void enableBluetooth(String type)
    {

        if (type.equals("auto")){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(intent,REQUEST_AUTO_ENABLE_BT);
        }else{
            if(bluetoothAdapter.isEnabled())
            {
//            Toast.makeText(mActivity.getApplicationContext(),"블루투스 활성화상태 입니다.", Toast.LENGTH_LONG).show();

                scanDevice();
            }
            else {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivity.startActivityForResult(intent,REQUEST_ENABLE_BT);
            }

        }

    }

    public void scanDevice()
    {
        Intent intent = new Intent(mActivity,DeviceListActivity.class);
        mActivity.startActivityForResult(intent,REQUEST_CONNECT_DEVICE);
    }

    public void getDeviceInfo(Intent data)
    {


        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object


//        if (preferences.getString("address","none").equals("none")){
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            connect(device);
//        }

        setAddress(address);

        // Get the device MAC address


        Log.e("device",address);



        //bluetoothAdapter.getBondedDevices().


    }

    public void autoPairing(String mac_add){
        Log.e("자동 연결","ㅎㅎ");

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(mac_add);

        connect(device);


//        byte[] buffer = null;
//        String str = "c";
//        buffer = str.getBytes();
//        write(buffer);
    }

    public void cancelBluetooth(){
        Log.e("cancel","취소");
        bluetoothAdapter.disable();
    }
}