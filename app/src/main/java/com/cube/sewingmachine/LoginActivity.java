package com.cube.sewingmachine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    Button login_btn,auto_login_btn,input_cancel_btn;
    EditText device_input;
    LinearLayout login_btn_back;
    boolean check = false;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_AUTO_ENABLE_BT = 3;

    public static Bluetooth bluetooth = null;

    public static boolean isConnected = false;
    private int chk_Bt_Connection = 0;

    String [] bluetooth_data = {"a","b","c","d","e","f"};

    LinearLayout not_exist_device_linear,empty_view1,button_top_empty_view;
    TextView not_exist_device_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        overridePendingTransition(0,0);

        preferences = getSharedPreferences("info",MODE_PRIVATE);
        editor = preferences.edit();

        View linearLayout=findViewById(R.id.login_container);
        Animation animation= AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        linearLayout.startAnimation(animation);

        login_btn = findViewById(R.id.login_btn);
        auto_login_btn = findViewById(R.id.auto_login_btn);
        input_cancel_btn = findViewById(R.id.input_cancel);

        device_input = findViewById(R.id.device_input);

        not_exist_device_linear = findViewById(R.id.no_exist_device_linear);
        not_exist_device_text = findViewById(R.id.no_exist_device_text);

        empty_view1 = findViewById(R.id.empty_view1);
        button_top_empty_view = findViewById(R.id.button_top_empty_view);

        login_btn_back = findViewById(R.id.login_btn_back);

        if (preferences.getBoolean("auto_login",false)){
            device_input.setText(preferences.getString("id","hello"));
            auto_login_btn.setBackgroundResource(R.drawable.auto_login_btn_on);
            check = true;
        }

        final View parent = (View) findViewById(R.id.touch_layout).getParent();
        parent.post( new Runnable() {

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
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)not_exist_device_linear.getLayoutParams();
                            layoutParams.weight = 30;
                            not_exist_device_linear.setLayoutParams(layoutParams);


                            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams)empty_view1.getLayoutParams();
                            layoutParams2.weight = 18;
                            empty_view1.setLayoutParams(layoutParams2);

                            LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams)button_top_empty_view.getLayoutParams();
                            layoutParams3.weight = 38;
                            button_top_empty_view.setLayoutParams(layoutParams3);

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
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"기기번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });



        if(bluetooth == null)bluetooth = new Bluetooth(this,handler);


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
        if ( !preferences.getString("address","none").equals("none") ){
            bluetooth.enableBluetooth("auto");
        }else{
            bluetooth.enableBluetooth("connect");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("hi","돈다");
            switch (msg.what) {
                case 0:
                case 1:
                    Log.d("BT", "disconnect");
                    Toast.makeText(getApplicationContext(), "블루투스연결이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("hello",bluetooth.getState()+"");
                    login_btn_back.setBackgroundResource(0);
                    login_btn.setBackgroundResource(R.drawable.login_btn_off);
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
                    login_btn.setEnabled(true);
                    login_btn_back.setBackgroundResource(R.drawable.login_btn_back);
                    login_btn.setBackgroundResource(R.drawable.login_btn);

                    if (preferences.getString("address","none").equals("none")){
                        editor.putString("address", bluetooth.getAddress());
                        editor.putBoolean("pairing",true);
                        editor.apply();
                    }

                    Log.e("데이터 저장","ㅎㅎ");
                    byte[] buffer = null;
                    String str = bluetooth_data[preferences.getInt("speed",0)];
//                    String str = "f";
                    Log.e("str",str+"");
                    buffer = str.getBytes();
                    bluetooth.write(buffer);
                    break;
                case 4:
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
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}