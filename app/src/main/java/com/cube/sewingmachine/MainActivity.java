package com.cube.sewingmachine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
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

    private ArrayList<Integer> imageList;
    private static final int DP = 24;

    LinearLayout start_embroidery, start_sewing, create_new, setting;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_embroidery = findViewById(R.id.start_embroidery);
        start_sewing = findViewById(R.id.start_sewing);
        create_new = findViewById(R.id.create_new);
        setting = findViewById(R.id.setting);

        start_embroidery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmbroideryActivity.class);
                startActivity(intent);
            }
        });

        start_sewing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SewingActivity.class);
                startActivity(intent);
            }
        });

        create_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        imageList = new ArrayList();

        imageList.add(R.drawable.auto_login_btn_off);
        imageList.add(R.drawable.auto_login_btn_on);
        imageList.add(R.drawable.ic_launcher_background);

        ViewPager viewPager = findViewById(R.id.viewPager);

//        float density = getResources().getDisplayMetrics().density;
//        int margin = (int) (DP * density);
//        viewPager.setPadding(margin, 0, margin, 0);
//        viewPager.setPageMargin(margin/2);

        viewPager.setAdapter(new ViewPagerAdapter(this, imageList));



//        Button bluetooth_btn = findViewById(R.id.bluetooth);
//        Button bluetooth_cancel = findViewById(R.id.bluetooth_cancel);

//        bluetooth_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bluetooth.enableBluetooth();
//            }
//        });
//
//        bluetooth_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bluetooth.cancelBluetooth();
//            }
//        });

//        if(bluetooth == null)bluetooth = new Bluetooth(this,handler);
//
//
//        if (bluetooth.getState() == 0){
//            Toast.makeText(getApplicationContext(),"블루투스를 연결해주세요", Toast.LENGTH_SHORT).show();
//        }
//
//
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

//        bluetooth.autoPairing("98:D3:A2:FD:74:B9");
    }

    public void initializeData()
    {


    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1:
                    Log.d("BT", "disconnect");
                    Toast.makeText(getApplicationContext(), "블루투스 연결이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Log.d("BT", "connecting");
                    break;
                case 3:
                    Log.d("BT", "connected");
                    Toast.makeText(getApplicationContext(), "블루투스가 연결되었습니다.", Toast.LENGTH_SHORT).show();
                    byte[] buffer = null;
                    String str = "abcd";
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