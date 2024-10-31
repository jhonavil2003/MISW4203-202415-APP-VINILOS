package com.example.app_vinilos_g17.ui.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.app_vinilos_g17.R

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(view.context)
            .load(url)
            .into(view)
    }
}