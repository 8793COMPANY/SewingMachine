package com.cube.sewingmachine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.Touch;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    Button login_btn,auto_login_btn,input_cancel_btn;
    EditText device_input;
    boolean check = false;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_AUTO_ENABLE_BT = 3;
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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        overridePendingTransition(0,0);

        preferences = getSharedPreferences("info",MODE_PRIVATE);
        // SharedPreferences 수정을 위한 Editor 객체를 얻어옵니다.
        editor = preferences.edit();

        View linearLayout=findViewById(R.id.login_container);
        Animation animation= AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        linearLayout.startAnimation(animation);

        login_btn = findViewById(R.id.login_btn);
        auto_login_btn = findViewById(R.id.auto_login_btn);
        input_cancel_btn = findViewById(R.id.input_cancel);

        device_input = findViewById(R.id.device_input);

        if (preferences.getBoolean("auto_login",false)){
            device_input.setText(preferences.getString("id","hello"));
            auto_login_btn.setBackgroundResource(R.drawable.auto_login_btn_on);
        }

        final View parent = (View) findViewById(R.id.touch_layout).getParent();
        parent.post( new Runnable() {
            // Post in the parent's message queue to make sure the parent
            // lays out its children before we call getHitRect()
            public void run() {
                Log.e("click","gg");
                Rect r = new Rect();

                auto_login_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("auto","click");
                        if (check){
                            auto_login_btn.setBackgroundResource(R.drawable.auto_login_btn_off);
                            check =false;
                        }else{
                            auto_login_btn.setBackgroundResource(R.drawable.auto_login_btn_on);
                            check=true;
                            editor.putBoolean("auto_login",true);
                            editor.apply();
                        }
                    }
                });

                auto_login_btn.getHitRect(r);

                r.top -= 200;
                r.left -=70;
                r.bottom += 200;
                r.right+=70;



                TouchDelegate touchDelegate = new TouchDelegate(r,auto_login_btn);

                if (View.class.isInstance(auto_login_btn.getParent())) {
                    ((View) auto_login_btn.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });


        device_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!device_input.getText().toString().equals("")){
                    input_cancel_btn.setVisibility(View.VISIBLE);
                }else{
                    input_cancel_btn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = device_input.getText().toString().trim();
                if (!id.equals("")){
                    editor.putString("id",id);
                    editor.apply();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



        if(bluetooth == null)bluetooth = new Bluetooth(this,handler);


        //bluetooth state check
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("hi","hi");

                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (chk_Bt_Connection != bluetooth.getState()) {
                        if(chk_Bt_Connection==2&&bluetooth.getState()==1){
                            chk_Bt_Connection = bluetooth.getState();
                        }
                        else {
                            chk_Bt_Connection = bluetooth.getState();
                            handler.sendEmptyMessage(chk_Bt_Connection);
                        }
                    }
                }
            }
        }).start();


        Log.e("hello",bluetooth.getState()+"");
//        if ( !preferences.getString("address","none").equals("none") ){
//            bluetooth.enableBluetooth("auto");
//        }else{
//            bluetooth.enableBluetooth("connect");
//        }



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                case 1:
                    Log.d("BT", "disconnect");
                    Toast.makeText(getApplicationContext(), "블루투스연결이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("hello",bluetooth.getState()+"");
                    bluetooth.enableBluetooth("connect");
                    break;
                case 2:
                    Log.d("BT", "connecting");
                    Toast.makeText(getApplicationContext(), "연결중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Log.d("BT", "connected");
                    Toast.makeText(getApplicationContext(), "블루투스가 연결되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("hello",bluetooth.getState()+"");

                    if (preferences.getString("address","none").equals("none")){
                        editor.putString("address", bluetooth.getAddress());
                        editor.putBoolean("pairing",true);
                        editor.apply();
                    }


                    Log.e("데이터 저장","ㅎㅎ");
                    byte[] buffer = null;
                    String str = "a";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case REQUEST_CONNECT_DEVICE:
                if(resultCode== Activity.RESULT_OK){
                    //reconnectAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        bluetooth.getDeviceInfo(data);
                }
                break;
            case REQUEST_ENABLE_BT:

                if(resultCode== Activity.RESULT_OK){
                    Log.e("REQUEST","ENABLE_BT");
                        bluetooth.scanDevice();
                }
                break;

            case REQUEST_AUTO_ENABLE_BT:

                if(resultCode== Activity.RESULT_OK){
                    Log.e("REQUEST","AUTO");

                    bluetooth.autoPairing(preferences.getString("address","none"));
//                    Toast.makeText(getApplicationContext(),"블루투스가 자동연결 되었습니다.",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
