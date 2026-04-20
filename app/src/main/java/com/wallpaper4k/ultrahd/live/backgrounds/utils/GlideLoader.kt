package com.wallpaper4k.ultrahd.live.backgrounds.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.wallpaper4k.ultrahd.live.backgrounds.R
import jp.wasabeef.glide.transformations.BlurTransformation

class GlideLoader//        circularProgressDrawable = new CircularProgressDrawable(mContext);
//        circularProgressDrawable.setStrokeWidth(5f);
//        circularProgressDrawable.setCenterRadius(20f);
//        circularProgressDrawable.setColorSchemeColors(
//                getColor(R.color.color_theme_blue),
//                getColor(R.color.color_theme_blue_70)
//        );
//        circularProgressDrawable.start();
//    private CircularProgressDrawable circularProgressDrawable;
    (private val mContext: Context?) {
    fun loadImage(imageUrl: String?, imageView: ImageView?) {
        if (mContext != null && imageView != null) {
            Glide.with(mContext).load(imageUrl).apply(
                RequestOptions().error(
                    R.color.transparent
                ).priority(Priority.HIGH)
            ).into(imageView)
        }
    }

    fun loadNotificationImage(drawable: Drawable?, imageView: ImageView?) {
        if (imageView != null && drawable != null) {
            imageView.setImageDrawable(drawable)
        }
    }

    //    void loadMediaImage(String imageUrl, ImageView imageView) {
    //        if (mContext != null && imageView != null) {
    //
    //            Glide.with(mContext).load(new File(imageUrl)).apply(
    //                    new RequestOptions()
    //                            .placeholder(circularProgressDrawable).error(
    //                                    R.color.transparent
    //                            ).priority(Priority.HIGH)
    //            ).into(imageView);
    //        }
    //    }
    fun loadBlurImage(imageUrl: String?, imageView: ImageView?) {
        if (mContext != null && imageView != null) {
            Glide.with(mContext).load(imageUrl)
                .transform(BlurTransformation(20))
                .apply(
                    RequestOptions().error(
                        R.color.transparent
                    ).priority(Priority.HIGH)
                )
                .into(imageView)
        }
    }
}