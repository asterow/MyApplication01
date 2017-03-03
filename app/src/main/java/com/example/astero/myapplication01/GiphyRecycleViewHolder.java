package com.example.astero.myapplication01;
/**
 * Created by astero on 26/02/2017.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class GiphyRecycleViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivGiphyMin;
    private ProgressBar pbGiphyMin;

    //itemView est la vue correspondante à 1 cellule
    public GiphyRecycleViewHolder(View itemView) {
        super(itemView);

        //c'est ici que l'on fait nos findView

        ivGiphyMin = (ImageView) itemView.findViewById(R.id.ivGiphyMin);
        pbGiphyMin = (ProgressBar) itemView.findViewById(R.id.pbGiphyMin);
    }

    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    public void bind(String URL){

//        Picasso.with(imageView.getContext()).load(GifURL).centerCrop().fit().into(imageView);
        pbGiphyMin.setVisibility(View.VISIBLE);
        Glide
                .with(ivGiphyMin.getContext())
                .load(URL).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                pbGiphyMin.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                pbGiphyMin.setVisibility(View.GONE);
                return false;
            }
        })
                .crossFade()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivGiphyMin);
    }
}