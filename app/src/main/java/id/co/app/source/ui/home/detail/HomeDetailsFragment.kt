package id.co.app.source.ui.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import id.co.app.source.R
import id.co.app.source.databinding.FragmentHomeBinding
import id.co.app.source.databinding.FragmentHomeDetailsBinding
import id.co.app.source.utilities.Common

class HomeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentHomeDetailsBinding
    private lateinit var cameraStr: String
    private var isToolbarShown = false
    private val isToolbarShownKey = "isToolbarShown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    private fun loadArguments() {
        arguments?.getString("PHOTO_NAME")?.let {
            cameraStr = it
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
            camera = cameraStr

        }
        setToolbar()
        checkToolbarStatus()
        return binding.root
    }

    private fun setToolbar() {
        val statusBarHeight = Common.statusBarHeight(requireActivity())
        val backdropHeight = (resources.getDimension(R.dimen.app_bar_backdrop_height) /
                resources.displayMetrics.density)
        val scrimHeightTrigger = backdropHeight + statusBarHeight
        binding.toolbarLayout.scrimVisibleHeightTrigger = scrimHeightTrigger.toInt() - 25
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val totalVerticalOffset = verticalOffset * -1
            val shouldShowToolbar = totalVerticalOffset >= totalScrollRange
            if (isToolbarShown != shouldShowToolbar) {
                isToolbarShown = shouldShowToolbar
                binding.appbar.isActivated = shouldShowToolbar
                binding.toolbarLayout.isTitleEnabled = shouldShowToolbar
                if (isToolbarShown) {
                    Common.setStatusColorDark(requireActivity())
                } else {
                    Common.setStatusColorLight(requireActivity())
                }
            }
        })
    }

    private fun checkToolbarStatus() {
        binding.appbar.isActivated = isToolbarShown
        binding.toolbarLayout.isTitleEnabled = isToolbarShown
        if (isToolbarShown) {
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
