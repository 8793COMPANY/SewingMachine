package com.cube.sewingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button login_btn,auto_login_btn,input_cancel_btn;
    EditText device_input;
    boolean check = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        overridePendingTransition(0,0);

        View linearLayout=findViewById(R.id.login_container);
        Animation animation= AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        linearLayout.startAnimation(animation);

        login_btn = findViewById(R.id.login_btn);
        auto_login_btn = findViewById(R.id.auto_login_btn);
        input_cancel_btn = findViewById(R.id.input_cancel);

        device_input = findViewById(R.id.device_input);


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
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        auto_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check){
                    auto_login_btn.setBackgroundResource(R.drawable.auto_login_btn_off);
                    check =false;
                }else{
                    auto_login_btn.setBackgroundResource(R.drawable.auto_login_btn_on);
                    check=true;
                }
            }
        });

//        ImageView logo = findViewById(R.id.logo_image);
//
//
//        Animation ani = AnimationUtils.loadAnimation(this,R.anim.logo_up);
//
//        linear.startAnimation(ani);
//        logo.startAnimation(ani);
    }
}
