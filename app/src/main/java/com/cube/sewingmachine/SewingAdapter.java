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

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    private ArrayList<Integer> mData = new ArrayList<>() ;
    int deviceWidth;
    int [] image_list_off = {R.drawable.sewing_one_image, R.drawable.sewing_two_image, R.drawable.sewing_three_image, R.drawable.sewing_four_image, R.drawable.sewing_five_image};
    int [] image_list_on = {R.drawable.sewing_one_image, R.drawable.sewing_two_image_on, R.drawable.sewing_three_image_on, R.drawable.sewing_four_image_on, R.drawable.sewing_five_image_on};


    public SewingAdapter(ArrayList<Integer> list,Activity activity){
        mData = list;
        DisplayMetrics displayMetrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        deviceWidth =(int) ((displayMetrics.widthPixels / (double) 720) * 204 ) ;  // 핸드폰의 가로 해상도를 구함.

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sewing_item_list, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("mData",mData.get(position)+"");
        holder.itemView.getLayoutParams().width = deviceWidth;
        holder.itemView.requestLayout();
        holder.imageView.setBackgroundResource(mData.get(position));

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
            imageView = itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    imageView.setBackgroundResource(image_list_on[getAdapterPosition()]);
                    mListener.onItemClick(v, getAdapterPosition()) ;
                }
            });


        }
    }

}
