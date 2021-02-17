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
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {



    Button start_embroid_btn, start_sewing_btn, create_new_btn, setting_btn_btn;
    ViewPager viewPager;

    WormDotsIndicator wormDotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        wormDotsIndicator = findViewById(R.id.worm_dots_indicator);
        start_embroid_btn = findViewById(R.id.start_embroidery);
        start_sewing_btn = findViewById(R.id.start_sewing);
        create_new_btn = findViewById(R.id.create_new);
        setting_btn_btn = findViewById(R.id.setting);

        viewPager = findViewById(R.id.viewPager);


        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        wormDotsIndicator.setViewPager(viewPager);

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


}