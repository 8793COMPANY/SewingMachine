package com.corporation8793.sewingmachine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingActivity extends AppCompatActivity {

    Button device_connect_cancel, back_btn, save_btn;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    SwitchCompat auto_login_switch , bluetooth_switch;

    SeekBar speed_seekbar, prepare_time_seekbar;

    TextView device_num;

    boolean bluetooth_check = false, login_check = false, device_connect_check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        preferences = getSharedPreferences("info",MODE_PRIVATE);
        // SharedPreferences 수정을 위한 Editor 객체를 얻어옵니다.
        editor = preferences.edit();

        device_connect_cancel = findViewById(R.id.device_connect_cancel_btn);
        back_btn = findViewById(R.id.back_btn);
        save_btn = findViewById(R.id.save_btn);
        device_num = findViewById(R.id.device_num);
        auto_login_switch = findViewById(R.id.auto_login_switch);
        bluetooth_switch = findViewById(R.id.bluetooth_switch);
        speed_seekbar = findViewById(R.id.speed_seekbar);
        prepare_time_seekbar = findViewById(R.id.prepare_time_seekbar);

        device_num.setText(preferences.getString("id","none"));


        if (preferences.getBoolean("auto_login",false)){
            auto_login_switch.setChecked(true);
            login_check = true;
        }

        if (preferences.getBoolean("pairing",false)){
            bluetooth_switch.setChecked(true);
            bluetooth_check = true;
        }

        setDeviceConnectBtn();


        speed_seekbar.setProgress(preferences.getInt("speed",0));

        prepare_time_seekbar.setProgress(preferences.getInt("time",1));

        device_connect_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("??","므여");
                device_connect_cancel.setBackgroundResource(R.drawable.login_btn_off);

                if (device_connect_check){
                    Toast.makeText(getApplicationContext(),"기기 연결이 해제되었습니다.",Toast.LENGTH_SHORT).show();
                    device_connect_cancel.setBackgroundResource(R.drawable.device_connect_btn);

                    auto_login_switch.setChecked(false);
                    bluetooth_switch.setChecked(false);
                    device_connect_check = false;
                }else{
                    device_connect_cancel.setBackgroundResource(R.drawable.device_connect_cancel_btn);
                    auto_login_switch.setChecked(true);
                    bluetooth_switch.setChecked(true);
                    device_connect_check = true;
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetooth_check)
                    editor.remove("address");

                if (!login_check)
                    editor.remove("id");
                editor.apply();
                finish();
            }
        });

        bluetooth_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bluetooth_check = isChecked;
//                setDeviceConnectBtn();
                if (isChecked){
                    editor.putBoolean("pairing",true);
                    setDeviceConnectBtn();
                }else{
//                    editor.remove("address");
                    editor.putBoolean("pairing",false);
                    if ((!login_check) && (!bluetooth_check)){
                        device_connect_cancel.setBackgroundResource(R.drawable.device_connect_btn);
                        device_connect_check = true;
                    }
                }

//                editor.apply();
            }
        });

        auto_login_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                login_check = isChecked;
//                setDeviceConnectBtn();
                if (isChecked){
                    editor.putBoolean("auto_login",true);
                    setDeviceConnectBtn();
                }else{
                    editor.putBoolean("auto_login",false);
                    if ((!login_check) && (!bluetooth_check)){
                        device_connect_cancel.setBackgroundResource(R.drawable.device_connect_btn);
                        device_connect_check = true;
                    }
                }
//                editor.apply();
            }
        });

        prepare_time_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("time",progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        speed_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                editor.putInt("speed",progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    void setDeviceConnectBtn(){
        Log.e("hi","gg");
        if (login_check || bluetooth_check){
            device_connect_cancel.setBackgroundResource(R.drawable.device_connect_cancel_btn);
            device_connect_check = true;
        }
    }

}
