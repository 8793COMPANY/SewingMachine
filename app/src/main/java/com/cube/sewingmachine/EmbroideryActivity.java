package com.cube.sewingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EmbroideryActivity extends AppCompatActivity {

    Button create_btn, modify_btn, start_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embroidery);

        create_btn = findViewById(R.id.create_btn);
        modify_btn = findViewById(R.id.modify_btn);
        start_btn = findViewById(R.id.start_btn);

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmbroideryActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmbroideryActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmbroideryActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

    }
}
