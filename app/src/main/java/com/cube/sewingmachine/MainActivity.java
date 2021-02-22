package com.cube.sewingmachine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.Calendar;

import static com.cube.sewingmachine.LoginActivity.bluetooth;




public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int MY_REQUEST_WRITE_STORAGE = 5;
    // Intent request code



    Button start_embroid_btn, start_sewing_btn, create_new_btn, setting_btn_btn;
    ViewPager viewPager;

    WormDotsIndicator wormDotsIndicator;


    AlertDialog finish_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDialog();
        wormDotsIndicator = findViewById(R.id.worm_dots_indicator);
        start_embroid_btn = findViewById(R.id.start_embroidery);
        start_sewing_btn = findViewById(R.id.start_sewing);
        create_new_btn = findViewById(R.id.create_new);
        setting_btn_btn = findViewById(R.id.setting);

        viewPager = findViewById(R.id.viewPager);


        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        wormDotsIndicator.setViewPager(viewPager);

        requestPerm();

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
                Intent intent = new Intent(MainActivity.this, StartEmbroidActivity.class);
                intent.putExtra("mode", "start");
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


    public void requestPerm() {
        // (시작) 권한 요청
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_REQUEST_WRITE_STORAGE);

            return;
        }
    }

    // TODO : 권한 요청
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // can run additional stuff here
                    //Toast.makeText(this, R.string.write_permission_granted, Toast.LENGTH_LONG).show();
                } else {
                    // permission denied
                    //Toast.makeText(this, R.string.write_permission_unavailable, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_factory_finish, null);
        LinearLayout cancel_btn, finish_btn;

        cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        finish_btn = dialogView.findViewById(R.id.finish_btn);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_dialog.dismiss();
            }
        });

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_dialog.dismiss();
                finish();
            }
        });

        builder.setView(dialogView);

        finish_dialog = builder.create();

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish_dialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Window window = finish_dialog.getWindow();

        int x = (int)(size.x *0.85f);
        int y = (int)(size.y *0.27f);

        window.setLayout(x,y);
    }



}