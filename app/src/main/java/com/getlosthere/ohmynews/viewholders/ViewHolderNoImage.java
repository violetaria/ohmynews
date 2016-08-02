package com.getlosthere.ohmynews.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.getlosthere.ohmynews.R;

/**
 * Created by violetaria on 8/1/16.
 */
public class ViewHolderNoImage  extends RecyclerView.ViewHolder {
    private TextView tvTitle;

    public ViewHolderNoImage(View itemView) {
        super(itemView);

        this.tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }
}
