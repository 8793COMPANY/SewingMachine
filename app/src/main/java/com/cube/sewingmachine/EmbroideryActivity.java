package com.cube.sewingmachine;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EmbroideryActivity extends AppCompatActivity {

    Button create_btn, modify_btn, start_btn;
    ScrollView scrollView;
    LinearLayout list_linear;

    int devicePixel;
    int viewPixcel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embroidery);

        create_btn = findViewById(R.id.create_btn);
        modify_btn = findViewById(R.id.modify_btn);
        start_btn = findViewById(R.id.start_btn);

        list_linear = findViewById(R.id.list_linear);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        devicePixel = (displayMetrics.widthPixels / 720) * 192;  // 핸드폰의 가로 해상도를 구함.
        viewPixcel = (displayMetrics.widthPixels / 720) *24;


        drawLinear(list_linear);
        drawColumnView(list_linear);

        drawLinear(list_linear);
        drawColumnView(list_linear);

        drawLinear(list_linear);
        drawColumnView(list_linear);

        drawLinear(list_linear);
        drawColumnView(list_linear);

        drawLinear(list_linear);
        drawColumnView(list_linear);




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

    void drawLinear(LinearLayout linear){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layout_p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, devicePixel);
        linearLayout.setLayoutParams(layout_p);

        drawImageview(linearLayout);
        drawWidthView(linearLayout);
        drawImageview(linearLayout);
        drawWidthView(linearLayout);
        drawImageview(linearLayout);


        linear.addView(linearLayout);
    }


    void drawImageview(LinearLayout linearLayout){
        //여기서 비트맵 이미지 넣고 이미지뷰마다 아이디 부여하고 싶으면 하시면 될 거 같아요 !
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.drawable.item_background);
        LinearLayout.LayoutParams layout_p2 = new LinearLayout.LayoutParams(devicePixel, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layout_p2);

        linearLayout.addView(imageView);
    }
    void drawWidthView(LinearLayout linearLayout){
        View view = new View(this);
        LinearLayout.LayoutParams layout_p2 = new LinearLayout.LayoutParams(viewPixcel, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layout_p2);

        linearLayout.addView(view);
    }

    void drawColumnView(LinearLayout linearLayout){
        View view = new View(this);
        LinearLayout.LayoutParams layout_p2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,viewPixcel);
        view.setLayoutParams(layout_p2);

        linearLayout.addView(view);
    }

}
