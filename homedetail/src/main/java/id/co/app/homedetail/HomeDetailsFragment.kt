/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.homedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.core.utilities.Common
import id.co.app.homedetail.databinding.FragmentHomeDetailsBinding

@AndroidEntryPoint
class HomeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentHomeDetailsBinding
    private lateinit var codeId: String
    private val codeIdKey = "CODE_KEY"
    private var isToolbarShown = false
    private val isToolbarShownKey = "isToolbarShown"

    private val args: HomeDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    private fun loadArguments() {
        arguments?.getString(codeIdKey)?.let {
            codeId = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState != null) {
            isToolbarShown = savedInstanceState.getBoolean(isToolbarShownKey)
        }
        binding = DataBindingUtil.inflate<FragmentHomeDetailsBinding>(
            inflater,
            R.layout.fragment_home_details,
            container,
            false
        ).apply {
            //viewModel = plantDetailViewModel
            lifecycleOwner = viewLifecycleOwner
            code = args.detailsName

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_share -> {
                        true
                    }
                    else -> false
                }
            }

        }
        setHasOptionsMenu(true)
        //setBackPressCallBack()
        setToolbar()
        checkToolbarStatus()
        return binding.root
    }

//    private fun setBackPressCallBack() {
//        requireActivity()
//            .onBackPressedDispatcher
//            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//
//                    // Do custom work here
//                    val cobastatus = isEnabled
//                    // if you want onBackPressed() to be called as normal afterwards
//                    if (isEnabled) {
//                        Toast.makeText(requireContext(), "test", Toast.LENGTH_SHORT).show()
//                        isEnabled = false
//                        requireActivity().onBackPressed()
//                    }
//                }
//            }
//            )
//    }

    private fun setToolbar() {
//        val statusBarHeight = Common.statusBarHeight(requireActivity())
//        val backdropHeight = (resources.getDimension(R.dimen.app_bar_backdrop_height) /
//                resources.displayMetrics.density)
//        val scrimHeightTrigger = backdropHeight + statusBarHeight
//        binding.toolbarLayout.scrimVisibleHeightTrigger = scrimHeightTrigger.toInt() - 25
//        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            val totalScrollRange = appBarLayout.totalScrollRange
//            val totalVerticalOffset = verticalOffset * -1
//            val shouldShowToolbar = totalVerticalOffset >= totalScrollRange
//            if (isToolbarShown != shouldShowToolbar) {
//                isToolbarShown = shouldShowToolbar
//                binding.appbar.isActivated = shouldShowToolbar
//                binding.toolbarLayout.isTitleEnabled = shouldShowToolbar
//                if (isToolbarShown) {
//                    Common.setStatusColorDark(requireActivity())
//                } else {
//                    Common.setStatusColorLight(requireActivity())
//                }
//            }
//        })

        binding.homeDetailScrollview.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                val shouldShowToolbar = scrollY > binding.toolbar.height
                if (isToolbarShown != shouldShowToolbar) {
                    isToolbarShown = shouldShowToolbar
                    checkToolbarStatus()
                }
            }
        )

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

    @Override
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(isToolbarShownKey, isToolbarShown)
        super.onSaveInstanceState(savedInstanceState)
    }

}
