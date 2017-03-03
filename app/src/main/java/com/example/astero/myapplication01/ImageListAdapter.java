package com.example.astero.myapplication01;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by astero on 25/02/2017.
 */

public class ImageListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<String> imageUrls;

    public ImageListAdapter(Context context, ArrayList<String> imageUrls) {

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateData(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
        this.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.card_view, parent, false);
        }
        final ProgressBar pvGiphyMin = (ProgressBar) convertView.findViewById(R.id.pbGiphyMin);
        final String URL = imageUrls.get(position);
        pvGiphyMin.setVisibility(View.VISIBLE);

        Glide
                .with(context)
                .load(URL).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                pvGiphyMin.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                pvGiphyMin.setVisibility(View.GONE);
                return false;
            }
        })
                .crossFade()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView) convertView.findViewById(R.id.ivGiphyMin));


        return convertView;
    }
}