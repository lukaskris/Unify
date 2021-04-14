/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.co.app.core.base.BaseAdapter
import id.co.app.core.base.BaseViewHolder
import id.co.app.core.domain.entities.Pokemon
import id.co.app.home.databinding.ItemListHomeBinding

class HomeAdapter (
    private val clickListener: (Pokemon) -> Unit
):
    BaseAdapter() {

    override fun createViewHolders(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        return PhotosViewHolder(
            ItemListHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        ) as BaseViewHolder<Any>
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