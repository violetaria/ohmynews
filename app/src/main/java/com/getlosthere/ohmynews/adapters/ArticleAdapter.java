package com.getlosthere.ohmynews.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getlosthere.ohmynews.R;
import com.getlosthere.ohmynews.helpers.DynamicHeightImageView;
import com.getlosthere.ohmynews.models.Article;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by violetaria on 7/31/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> articles;
    private Context mContext;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements Target {
        public DynamicHeightImageView ivImage;
        public TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            this.ivImage = (DynamicHeightImageView) itemView.findViewById(R.id.ivImage);
            this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // Calculate the image ratio of the loaded bitmap
            float ratio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
            // Set the ratio for the image
            ivImage.setHeightRatio(ratio);
            // Load the image into the view
            ivImage.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.article_item_result, parent, false);

        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder viewHolder, int position) {
        Article article = articles.get(position);

        // Set item views based on your views and data model
        TextView tv = viewHolder.tvTitle;
        ImageView iv = viewHolder.ivImage;

        tv.setText(article.getHeadline());

        iv.setImageResource(0);

        String thumbnail = article.getThumbnail();

        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).fit().centerCrop().into(iv);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
