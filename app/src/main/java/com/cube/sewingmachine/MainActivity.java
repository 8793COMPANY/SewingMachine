package com.cube.sewingmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    // Intent request code

    //bluetooth
    public static Bluetooth bluetooth = null;
    //bluetooth

    // buletooth connect check
    ProgressDialog dialog;
    public static boolean isConnected = false;
    // buletooth connect check

    // bluetooth state check
    private int chk_Bt_Connection = 0;

    Button start_embroid_btn, start_sewing_btn, create_new_btn, setting_btn_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_embroid_btn = findViewById(R.id.start_embroidery);
        start_sewing_btn = findViewById(R.id.start_sewing);
        create_new_btn = findViewById(R.id.create_new);
        setting_btn_btn = findViewById(R.id.setting);

        start_embroid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmbroideryActivity.class);
                startActivity(intent);
            }
        });

        start_sewing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SewingActivity.class);
                startActivity(intent);
            }
        });

        create_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        setting_btn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        //bluetooth state check
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (chk_Bt_Connection != bluetooth.getState()) {
//                        if(chk_Bt_Connection==2&&bluetooth.getState()==1){
//                            chk_Bt_Connection = bluetooth.getState();
//                        }
//                        else {
//                            chk_Bt_Connection = bluetooth.getState();
//                            handler.sendEmptyMessage(chk_Bt_Connection);
//                        }
//                    }
//                }
//            }
//        }).start();

    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1:
                    Log.d("BT", "disconnect");
                    Toast.makeText(getApplicationContext(), "블루투스연결이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Log.d("BT", "connecting");
                    break;
                case 3:
                    Log.d("BT", "connected");
                    Toast.makeText(getApplicationContext(), "블루투스가 연결되었습니다.", Toast.LENGTH_SHORT).show();
                    byte[] buffer = null;
                    String day = null;
                    if((Calendar.getInstance().get(Calendar.MONTH)+1 == 1) || (Calendar.getInstance().get(Calendar.MONTH)+1 == 3) || (Calendar.getInstance().get(Calendar.MONTH)+1 == 5) || (Calendar.getInstance().get(Calendar.MONTH)+1 == 7) || (Calendar.getInstance().get(Calendar.MONTH)+1 == 8) || (Calendar.getInstance().get(Calendar.MONTH)+1 == 10) || (Calendar.getInstance().get(Calendar.MONTH)+1 == 12)){
                        day = "31";
                    }
                    else if((Calendar.getInstance().get(Calendar.MONTH)+1 == 4) || (Calendar.getInstance().get(Calendar.MONTH)+1 == 6) || (Calendar.getInstance().get(Calendar.MONTH)+1 == 9) || (Calendar.getInstance().get(Calendar.MONTH)+1 == 11)){
                        day = "30";
                    }
                    else{
                        if((Calendar.getInstance().get(Calendar.YEAR) % 4 == 0 && Calendar.getInstance().get(Calendar.YEAR) % 100 != 0) || Calendar.getInstance().get(Calendar.YEAR) % 400 == 0){
                            day = "29";
                        }
                        else{
                            day = "28";
                        }
                    }
                    String str = "set" + day + "," + String.valueOf(Calendar.getInstance().get(Calendar.DATE));
                    buffer = str.getBytes();
                    bluetooth.write(buffer);
                    break;
                case 4: //수신한 데이터처리는 이곳에서
                    Log.e("BT", "data recieve");
                    byte[] readBuf = (byte[]) msg.obj;
                    String readStr = new String(readBuf);
                    Log.e("readStr",readStr.trim());

                    break;
            }
        }
    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode)
//        {
//            case REQUEST_CONNECT_DEVICE:
//                if(resultCode== Activity.RESULT_OK){
//                    //reconnectAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                    bluetooth.getDeviceInfo(data);
//                }
//                break;
//            case REQUEST_ENABLE_BT:
//                if(resultCode== Activity.RESULT_OK){
//                    bluetooth.scanDevice();
//                }
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}