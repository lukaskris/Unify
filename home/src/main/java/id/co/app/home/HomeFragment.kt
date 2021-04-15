/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.core.deeplink.InternalDeepLink
import id.co.app.core.domain.entities.Pokemon
import id.co.app.core.extension.onFailure
import id.co.app.core.extension.onSuccess
import id.co.app.core.utilities.Common
import id.co.app.home.databinding.FragmentHomeBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var moshi: Moshi

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    private var localJob: Job? = null

    private val optionsTransition by lazy {
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }

    private val homeAdapter by lazy { HomeAdapter {
        val json = moshi.adapter(Pokemon::class.java).toJson(it)
        val deepLink = InternalDeepLink.HOME_DETAIL.format(json).toUri()
        Navigation.findNavController(binding.root).navigate(deepLink, optionsTransition)
    } }

    private var isToolbarShown = false
    private val isToolbarShownKey = "isToolbarShown"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState != null) {
            isToolbarShown = savedInstanceState.getBoolean(isToolbarShownKey)
        }
        binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
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
        observeLiveData()
    }

    private fun initBindings() {
        val linearLayoutManager = LinearLayoutManager(activity)
        binding.homePhotosList.apply {
            adapter = homeAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun observeLiveData(){
        localJob = lifecycleScope.launch {
            homeViewModel.pokemonListFlow.collect { result ->
                result.onSuccess {
                    if(it.firstOrNull()?.page != 0) homeAdapter.appendList(it)
                    else homeAdapter.submitList(it)
                }

                result.onFailure {
                    Toast.makeText(context, "Please check your connection issues", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @Override
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(isToolbarShownKey, isToolbarShown)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        localJob?.cancel()
        super.onDestroy()
    }
}