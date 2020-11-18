package id.co.app.source.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.co.app.source.R
import id.co.app.source.databinding.FragmentHomeBinding
import id.co.app.source.utilities.Common

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = FragmentHomeBindingImpl.inflate(inflater)
        binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        ).apply {
            //viewModel = plantDetailViewModel
            lifecycleOwner = viewLifecycleOwner
            var isToolbarShown = false
            toolbarLayout.isTitleEnabled = true
            homePhotosList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    Log.v("dy", "dy : $dy")
                    val shouldShowToolbar = dy > 0
                    if (isToolbarShown != shouldShowToolbar) {
                        isToolbarShown = shouldShowToolbar

                        // Use shadow animator to add elevation if toolbar is shown
                        appbar.isActivated = shouldShowToolbar

                        // Show the plant name if toolbar is shown
                        toolbarLayout.isTitleEnabled = shouldShowToolbar
                        if (isToolbarShown) {
                            Common.setStatusColorDark(activity!!)
                        } else {
                            Common.setStatusColorLight(activity!!)
                        }
                    }

                }
            })

//            toolbar.setNavigationOnClickListener { view ->
//                view.findNavController().navigateUp()
//            }
//            toolbar.setOnMenuItemClickListener { item ->
//                when (item.itemId) {
//                    R.id.action_share -> {
//                        //createShareIntent()
//                        true
//                    }
//                    else -> false
//                }
//            }
        }
//        setHasOptionsMenu(true)
        Common.setStatusColorLight(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
    }

    private fun initBindings() {
        val photosAdapter = PhotosAdapter(getPhotosList())
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.homePhotosList.apply {
            adapter = photosAdapter
            layoutManager = linearLayoutManager

        }
    }

    private fun getPhotosList(): List<String> {
        val photosList = mutableListOf<String>()
        for (i in 0..25) {
            photosList.add("Photo $i")
        }
        return photosList
    }

}