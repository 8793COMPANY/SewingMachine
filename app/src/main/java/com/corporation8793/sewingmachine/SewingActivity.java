package com.corporation8793.sewingmachine;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SewingActivity extends AppCompatActivity {

    int [] image_list ={R.drawable.sewing_one_image,R.drawable.sewing_three_image, R.drawable.sewing_five_image,  R.drawable.sewing_four_image, R.drawable.sewing_two_image};
    int [] image_list_on = {R.drawable.sewing_one_image_on,R.drawable.sewing_three_image_on, R.drawable.sewing_five_image_on,  R.drawable.sewing_four_image_on, R.drawable.sewing_two_image_on};
    int [] sewing_text_box_image = {R.drawable.sewing_one_text,R.drawable.sewing_three_text,R.drawable.sewing_five_text,R.drawable.sewing_four_text,R.drawable.sewing_two_text};
    Button back_btn, finish_btn, start_btn;
    ImageView sewing_text_box;
    int pos= 0;
    public static Bluetooth bluetooth = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sewing);
        if(bluetooth == null)bluetooth = LoginActivity.bluetooth;



        ArrayList<Integer> list = new ArrayList<>();

        list.add(R.drawable.sewing_one_image_on);
        for (int i=1; i<5; i++) {
            Log.e("??",image_list[i]+"");
            list.add(image_list[i]) ;
        }
        Log.e("count",list.size()+"");

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recyclerview) ;
        finish_btn = findViewById(R.id.finish_btn);
        back_btn = findViewById(R.id.back_btn);
        start_btn = findViewById(R.id.sewing_start_btn);
        sewing_text_box = findViewById(R.id.sewing_text_box);

        Log.e("bluetooth",bluetooth.getState()+"");
        if (bluetooth.getState() == 3){
            start_btn.setBackgroundResource(R.drawable.sewing_start_btn_on);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)) ;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

//        RecyclerDecoration spaceDecoration = new RecyclerDecoration((int) ((displayMetrics.widthPixels / (double) 720) * 30 ));
//        recyclerView.addItemDecoration(spaceDecoration);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        SewingAdapter adapter = new SewingAdapter(list , this) ;
        recyclerView.setAdapter(adapter) ;

        adapter.setOnItemClickListener(new SewingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                list.set(pos,image_list[pos]);
                list.set(position,image_list_on[position]);
                adapter.notifyDataSetChanged();
                sewing_text_box.setBackgroundResource(sewing_text_box_image[position]);
                pos=position;
            }
        });

        Log.e("recyclerview",recyclerView.getHeight()+"");

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bluetooth.write();
            }
        });



    }
}
