/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.source.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.source.R
import id.co.app.source.databinding.FragmentHomeBinding
import id.co.app.source.utilities.Common

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var isToolbarShown = false
    private val isToolbarShownKey = "isToolbarShown"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState != null) {
            isToolbarShown = savedInstanceState.getBoolean(isToolbarShownKey)
        }
        //binding = FragmentHomeBindingImpl.inflate(inflater)
        binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        ).apply {
            //viewModel = plantDetailViewModel
            lifecycleOwner = viewLifecycleOwner

        }
        setToolbar()
//        setHasOptionsMenu(true)
        checkToolbarStatus()
        return binding.root
    }

    private fun setToolbar() {
        val statusBarHeight = Common.statusBarHeight(requireActivity())
        val backdropHeight = (resources.getDimension(R.dimen.app_bar_backdrop_height) /
                resources.displayMetrics.density)
        //val backdropHeight = resources.getDimension(R.dimen.plant_detail_app_bar_height)
        val scrimHeightTrigger = backdropHeight + statusBarHeight
        //val scrimHeightTrigger = 240
        binding.toolbarLayout.scrimVisibleHeightTrigger = scrimHeightTrigger.toInt() - 20
        //val toolbarHeight =  calculateActionBar()
        //toolbarLayout.scrimVisibleHeightTrigger = scrimHeightTrigger.toInt() - toolbarHeight
        //toolbarLayout.scrimVisibleHeightTrigger = 0
//            Log.v(
//                "scrimHeightTrigger", "statusbar : " +
//                        "$statusBarHeight, backdropHeight: $backdropHeight, " +
//                        "scrimHeightTrigger : $scrimHeightTrigger, " +
//                        "toolbarHeight: $toolbarHeight"
//            )
        binding.appbar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
//                val midTotalScrollRange = totalScrollRange / 2
            val totalVerticalOffset = verticalOffset * -1
            //Log.v("verticalOffset", "verticalOffset : $totalVerticalOffset , $totalScrollRange")
//                val color = changeAlpha(
//                    ContextCompat.getColor(requireContext(), R.color.white),
//                    abs(verticalOffset * 1.0f) / totalScrollRange
//                )
            //toolbar.setBackgroundColor(color)
            val shouldShowToolbar = totalVerticalOffset >= totalScrollRange
            if (isToolbarShown != shouldShowToolbar) {
                isToolbarShown = shouldShowToolbar
                checkToolbarStatus()
            }

            //toolbarLayout.setStatusBarScrimColor(color)
        })
        binding.homePhotosList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //Log.v("dy", "dy : $dy")
//                    val shouldShowToolbar = dy > 0
//                    if (isToolbarShown != shouldShowToolbar) {
//                        isToolbarShown = shouldShowToolbar
//
//                        // Use shadow animator to add elevation if toolbar is shown
//                        appbar.isActivated = shouldShowToolbar
//
//                        // Show the plant name if toolbar is shown
//                        toolbarLayout.isTitleEnabled = shouldShowToolbar
//                        if (isToolbarShown) {
//                            Common.setStatusColorDark(activity!!)
//                        } else {
//                            Common.setStatusColorLight(activity!!)
//                        }
//                    }
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

    private fun checkToolbarStatus() {
        binding.appbar.isActivated = isToolbarShown
        binding.toolbarLayout.isTitleEnabled = isToolbarShown
        if (isToolbarShown) {
            if (!activity?.let { Common.isDarkModeOn(it) }!!)
                Common.setStatusColorDark(requireActivity())
        } else {
            Common.setStatusColorLight(requireActivity())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
    }

    private fun initBindings() {
        val photosAdapter = HomeAdapter(getPhotosList())
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.homePhotosList.apply {
            adapter = photosAdapter
            layoutManager = linearLayoutManager

        }
    }

    private fun getPhotosList(): List<String> {
        val photosList = mutableListOf<String>()
        for (i in 0..25) {
            photosList.add("List $i")
        }
        return photosList
    }

    @Override
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(isToolbarShownKey, isToolbarShown)
        super.onSaveInstanceState(savedInstanceState)
    }

}