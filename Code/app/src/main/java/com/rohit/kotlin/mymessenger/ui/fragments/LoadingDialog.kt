package com.rohit.kotlin.mymessenger.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.rohit.kotlin.mymessenger.R

class LoadingDialog(val context: Activity) {
    var dialog: Dialog? = null

    fun showProgressDialog() {
        dialog = Dialog(context)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.loading_indicator)

        val loadingImageView = dialog?.findViewById<ImageView>(R.id.custom_loading_imageView)
        Glide.with(context)
            .load(R.drawable.loader_ring_indicator)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
            .into(DrawableImageViewTarget(loadingImageView));

        dialog?.show()
    }

    fun dismissProgressDialog() {
        dialog?.dismiss()
    }
}