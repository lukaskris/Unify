package id.co.app.source.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import id.co.app.source.databinding.ItemPhotoHomeBinding

class PhotosAdapter(private val photosList: List<String>) :
    RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            ItemPhotoHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = photosList.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        holder.bind(photosList[position])
    }

    class PhotosViewHolder(rowBinding: ItemPhotoHomeBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {
        private val binding = rowBinding

        fun bind(str: String) {
            binding.camera = str
            //val bundle = bundleOf("PHOTO_NAME" to str)
            binding.root.setOnClickListener { view ->
                val direction =
                    HomeFragmentDirections.actionHomeToDetails(str)
                //Navigation.findNavController(view).navigate(R.id.action_home_to_details, bundle)
                //findNavController().navigate(direction)
                Navigation.findNavController(view).navigate(direction)
            }
        }
    }
}