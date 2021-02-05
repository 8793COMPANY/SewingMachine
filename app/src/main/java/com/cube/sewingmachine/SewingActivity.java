package com.cube.sewingmachine;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SewingActivity extends AppCompatActivity {

    int [] image_list = {R.drawable.sewing_one_click, R.drawable.sewing_two_image, R.drawable.sewing_three_image, R.drawable.sewing_four_image, R.drawable.sewing_five_image};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sewing);

        ArrayList<Integer> list = new ArrayList<>();

        for (int i=0; i<5; i++) {
            Log.e("??",image_list[i]+"");
            list.add(image_list[i]) ;
        }
        Log.e("count",list.size()+"");

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recyclerview) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)) ;



        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        SewingAdapter adapter = new SewingAdapter(list , this) ;
        recyclerView.setAdapter(adapter) ;
    }
}
