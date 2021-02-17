package com.cube.sewingmachine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    Button device_connect_cancel, back_btn, save_btn;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    Switch auto_login_switch, bluetooth_switch;

    SeekBar speed_seekbar, prepare_time_seekbar;

    TextView device_num;



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

        }

        if (preferences.getBoolean("pairing",false)){
            bluetooth_switch.setChecked(true);
        }

        speed_seekbar.setProgress(preferences.getInt("speed",0));

        prepare_time_seekbar.setProgress(preferences.getInt("time",1));

        device_connect_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("address");
                editor.commit();
                Toast.makeText(getApplicationContext(),"기기 연결이 해제되었습니다.",Toast.LENGTH_SHORT).show();
                auto_login_switch.setChecked(false);
                bluetooth_switch.setChecked(false);
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
                editor.apply();
                finish();
            }
        });

        bluetooth_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putBoolean("pairing",true);
                }else{
                    editor.remove("address");
                    editor.putBoolean("pairing",false);
                }
//                editor.apply();
            }
        });

        auto_login_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putBoolean("auto_login",true);
                }else{
                    editor.remove("id");
                    editor.putBoolean("auto_login",false);
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
}
