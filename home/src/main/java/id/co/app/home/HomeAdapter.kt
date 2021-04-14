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
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.squareup.moshi.Moshi
import id.co.app.core.base.BaseAdapter
import id.co.app.core.base.BaseViewHolder
import id.co.app.core.deeplink.InternalDeepLink
import id.co.app.core.domain.entities.Pokemon
import id.co.app.home.databinding.ItemListHomeBinding

class HomeAdapter (
    private val clickListener: (Pokemon) -> Unit
):
    BaseAdapter<HomeAdapter.PhotosViewHolder, Pokemon>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            ItemListHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )
    }

    class PhotosViewHolder(rowBinding: ItemListHomeBinding, private val clickListener: (Pokemon) -> Unit) :
        BaseViewHolder<Pokemon>(rowBinding.root) {
        private val binding = rowBinding
        override fun bind(data: Pokemon) {
            binding.item = data
            binding.clickListener = View.OnClickListener {
                clickListener.invoke(data)
            }
        }
    }
}