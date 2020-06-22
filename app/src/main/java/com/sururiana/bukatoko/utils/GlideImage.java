package com.sururiana.bukatoko.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.sururiana.bukatoko.R;

public class GlideImage {
    public static void get(Context context, String urlImage, ImageView imageView){
        RequestOptions options = new RequestOptions().centerCrop().placeholder(R.drawable.ic_load).error(R.drawable.ic_close)
                .priority(Priority.HIGH);

        Glide.with(context).load(urlImage).apply(options).into(imageView);
    }
}
