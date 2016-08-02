package com.getlosthere.ohmynews.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getlosthere.ohmynews.R;
import com.getlosthere.ohmynews.models.Article;
import com.getlosthere.ohmynews.viewholders.ViewHolderImage;
import com.getlosthere.ohmynews.viewholders.ViewHolderNoImage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by violetaria on 8/1/16.
 */
public class ComplexArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> articles;

    private final int IMAGE=1, NO_IMAGE=0;

    public ComplexArticleAdapter(List<Object> articles) {
        this.articles = articles;
    }

    @Override
    public int getItemCount() {
        return this.articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        Article article = (Article) articles.get(position);

        if (TextUtils.isEmpty(article.getThumbnail())) {
            return NO_IMAGE;
        } else {
            return IMAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType){
            case IMAGE:
                View v1 = inflater.inflate(R.layout.layout_viewholderimage, viewGroup, false);
                viewHolder = new ViewHolderImage(v1);
                break;
            case NO_IMAGE:
                View v2 = inflater.inflate(R.layout.layout_viewholdernoimage, viewGroup, false);
                viewHolder = new ViewHolderNoImage(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.layout_viewholdernoimage, viewGroup, false);
                viewHolder = new ViewHolderNoImage(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch(viewHolder.getItemViewType()) {
            case IMAGE:
                ViewHolderImage vh1 = (ViewHolderImage) viewHolder;
                configureViewHolderImage(vh1, position);
                break;
            case NO_IMAGE:
                ViewHolderNoImage vh2 = (ViewHolderNoImage) viewHolder;
                configureViewHolderNoImage(vh2, position);
                break;
            default:
                ViewHolderNoImage vh = (ViewHolderNoImage) viewHolder;
                configureViewHolderNoImage(vh, position);
                break;
        }
    }

    private void configureViewHolderImage(ViewHolderImage viewHolder, int position) {
        Article article = (Article) articles.get(position);
        if( article!= null) {
            viewHolder.getTvTitle().setText(article.getHeadline());
            viewHolder.getIvImage().setImageResource(0);

            String thumbnail = article.getThumbnail();

            if(!TextUtils.isEmpty(thumbnail)){
                Picasso.with(viewHolder.getIvImage().getContext()).load(thumbnail).fit().centerCrop().into(viewHolder.getIvImage());
            }
        }
    }
    private void configureViewHolderNoImage(ViewHolderNoImage viewHolder, int position) {
        Article article = (Article) articles.get(position);
        if( article!= null) {
            viewHolder.getTvTitle().setText(article.getHeadline());
        }
    }
}
