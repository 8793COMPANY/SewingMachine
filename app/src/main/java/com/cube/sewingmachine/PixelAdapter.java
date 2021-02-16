package com.cube.sewingmachine;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class PixelAdapter extends RecyclerView.Adapter<PixelAdapter.ViewHolder> {

    private Bitmap[] localDataSet;

    static MaterialCardView lastChecked = null;
    public static int currentCheckedPos = 0;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pixel_thumb;
        MaterialCardView pixel_card;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            pixel_thumb = view.findViewById(R.id.pixel_thumb);
            pixel_card = view.findViewById(R.id.pixel_card);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pixel_card.isSelected()) {
                    } else {
                        currentCheckedPos = getAdapterPosition();
                        pixel_card.setSelected(true);
                        pixel_card.setStrokeColor(0xFF15d4e4);
                        pixel_card.setStrokeWidth(6);

                        lastChecked.setSelected(false);
                        lastChecked.setStrokeWidth(0);
                        lastChecked = pixel_card;
                    }

                    Log.e("눌러!", "커첵: " + currentCheckedPos);
                }
            });
        }
    }

    public int getCurrentCheckedPos() {
        return currentCheckedPos;
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public PixelAdapter(Bitmap[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sewing_recyclerview_ltem, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.pixel_thumb.setImageBitmap(localDataSet[position]);
        viewHolder.pixel_card.setSelected(false);

        if (position == 0) {
            viewHolder.pixel_card.setSelected(true);
            viewHolder.pixel_card.setStrokeColor(0xFF15d4e4);
            viewHolder.pixel_card.setStrokeWidth(6);
            lastChecked = viewHolder.pixel_card;
            currentCheckedPos = 0;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
