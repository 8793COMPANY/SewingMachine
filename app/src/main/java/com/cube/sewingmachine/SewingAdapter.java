package com.cube.sewingmachine;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SewingAdapter extends RecyclerView.Adapter<SewingAdapter.ViewHolder> {
    private ArrayList<Integer> mData = null ;
    int deviceWidth;


    public SewingAdapter(ArrayList<Integer> list,Activity activity){
        mData = list;
        DisplayMetrics displayMetrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);



        deviceWidth = (displayMetrics.widthPixels / 720) * 204  ;  // 핸드폰의 가로 해상도를 구함.
        Log.e("deviceWidth",deviceWidth+"");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sewing_recyclerview_ltem, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setBackgroundResource(mData.get(position));


        holder.itemView.getLayoutParams().width = deviceWidth;

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            imageView = itemView.findViewById(R.id.imageview) ;
        }
    }
}
