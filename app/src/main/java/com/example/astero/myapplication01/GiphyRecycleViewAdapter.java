package com.example.astero.myapplication01;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by astero on 26/02/2017.
 */

public class GiphyRecycleViewAdapter extends RecyclerView.Adapter<GiphyRecycleViewHolder> {

    List<String> listImageURL;

    //ajouter un constructeur prenant en entrée une liste
    public GiphyRecycleViewAdapter(List<String> listImageURL) {
        this.listImageURL = listImageURL;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public GiphyRecycleViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new GiphyRecycleViewHolder(view);
    }

    //c'est ici que nous allons remplir notre cellule avec le texte/image de chaque MyObjects
    @Override
    public void onBindViewHolder(GiphyRecycleViewHolder giphyRecycleViewHolder, int position) {
        String GiphyURL = listImageURL.get(position);
        giphyRecycleViewHolder.bind(GiphyURL);
    }

    @Override
    public int getItemCount() {
        return listImageURL.size();
    }

    public String getItem(int position) {
        return listImageURL.get(position);
    }

    public void add(int position, String URL) {
        listImageURL.add(position, URL); // on insère le nouvel objet dans notre       liste d'article lié à l'adapter
        notifyItemInserted(position); // on notifie à l'adapter ce changement
    }



}