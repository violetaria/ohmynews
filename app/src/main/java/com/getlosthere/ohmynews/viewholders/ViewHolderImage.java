package com.getlosthere.ohmynews.viewholders;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.getlosthere.ohmynews.R;
import com.getlosthere.ohmynews.helpers.DynamicHeightImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by violetaria on 8/1/16.
 */
public class ViewHolderImage extends RecyclerView.ViewHolder implements Target {
    private DynamicHeightImageView ivImage;
    private TextView tvTitle;

    public ViewHolderImage(View itemView) {
        super(itemView);

        this.ivImage = (DynamicHeightImageView) itemView.findViewById(R.id.ivImage);
        this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }


    public DynamicHeightImageView getIvImage() {
        return ivImage;
    }

    public void setIvImage(DynamicHeightImageView ivImage) {
        this.ivImage = ivImage;
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
