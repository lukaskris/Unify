/*
 * Created by Hendry Syamsudin on 22/12/20 17:34
 * Copyright (c) APP Sinar Mas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.source.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import id.co.app.source.databinding.ItemListHomeBinding

class HomeAdapter(private val photosList: List<String>) :
    RecyclerView.Adapter<HomeAdapter.PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            ItemListHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = photosList.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val nameItem = photosList[position]
        val listener = View.OnClickListener { view ->
            val direction =
                HomeFragmentDirections.actionHomeToDetails(nameItem)
            Navigation.findNavController(view).navigate(direction)
        }
        holder.bind(nameItem, listener)
    }

    class PhotosViewHolder(rowBinding: ItemListHomeBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {
        private val binding = rowBinding
        fun bind(str: String, listener: View.OnClickListener?) {
            binding.item = str
            //val bundle = bundleOf("PHOTO_NAME" to str)
            binding.clickListener = listener
//            binding.parentViewList.setOnClickListener { view ->
//                val direction =
//                    HomeFragmentDirections.actionHomeToDetails(str)
//                //Navigation.findNavController(view).navigate(R.id.action_home_to_details, bundle)
//                //findNavController().navigate(direction)
//                Navigation.findNavController(view).navigate(direction)
//            }
        }
    }
}