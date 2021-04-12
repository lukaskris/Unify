/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.Navigation
import id.co.app.core.base.BaseAdapter
import id.co.app.core.base.BaseViewHolder
import id.co.app.core.deeplink.InternalDeepLink
import id.co.app.home.databinding.ItemListHomeBinding

class HomeAdapter :
    BaseAdapter<HomeAdapter.PhotosViewHolder, String>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            ItemListHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class PhotosViewHolder(rowBinding: ItemListHomeBinding) :
        BaseViewHolder<String>(rowBinding.root) {
        private val binding = rowBinding
        override fun bind(data: String) {
            binding.item = data
            //val bundle = bundleOf("PHOTO_NAME" to str)
            binding.clickListener = View.OnClickListener {
                val deepLink = InternalDeepLink.HOME_DETAIL.format(data).toUri()
                Navigation.findNavController(it).navigate(deepLink)
            }
        }
    }
}