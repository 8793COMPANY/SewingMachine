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
    int [] image_list = {R.drawable.sewing_one_image, R.drawable.sewing_two_image, R.drawable.sewing_three_image, R.drawable.sewing_four_image, R.drawable.sewing_five_image};

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.e("??","뭔데");
        View view = null;
        if (context != null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.viewpager_list, container, false);
            ImageView imageView = view.findViewById(R.id.imageview);
//            imageView.setBackgroundResource(R.drawable.item_background);
        }

        container.addView(view);

        return view;


    }


    @Override
    public int getCount() {
        return image_list.length;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}
