package com.getlosthere.ohmynews.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.getlosthere.ohmynews.R;
import com.getlosthere.ohmynews.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by violetaria on 7/26/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for position
        Article article = getItem(position);

        // check to see if existing view is being reused
        // not using a recycled view -> inflate the layout
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.article_item_result, parent, false);
        }

        //find the image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        // clear out recycled image from convertView from last time
        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(article.getHeadline());

        String thumbnail = article.getThumbnail();

        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView;
    }
}
