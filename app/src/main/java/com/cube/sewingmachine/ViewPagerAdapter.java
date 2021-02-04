package com.cube.sewingmachine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<Integer> imageList;

    public ViewPagerAdapter(Context context, ArrayList<Integer> imageList){
        this.context = context;
        this.imageList = imageList;

        Log.e("!","뭔데");
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.e("??","뭔데");

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_list, null);

        Log.e("imageList",imageList.get(position)+"");
        ImageView imageView = view.findViewById(R.id.imageview);
        imageView.setBackgroundResource(imageList.get(position));
        container.addView(view);

        return view;


    }


    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
