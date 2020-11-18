package id.co.app.source.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import id.co.app.source.R
import id.co.app.source.databinding.FragmentHomeBinding
import id.co.app.source.utilities.Common
import kotlin.math.abs


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var isToolbarShown = false
    private val isToolbarShownKey = "isToolbarShown"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            val scrimHeightTrigger = (resources.getDimension(R.dimen.plant_detail_app_bar_height) /
                    resources.displayMetrics.density)
            toolbarLayout.scrimVisibleHeightTrigger = scrimHeightTrigger.toInt()
            appbar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val totalScrollRange = appBarLayout.totalScrollRange
                val midTotalScrollRange = totalScrollRange / 2
                val totalVerticalOffset = verticalOffset * -1
                Log.v("verticalOffset", "verticalOffset : $totalVerticalOffset , $totalScrollRange")
//                val color = changeAlpha(
//                    ContextCompat.getColor(requireContext(), R.color.white),
//                    abs(verticalOffset * 1.0f) / totalScrollRange
//                )
                //toolbar.setBackgroundColor(color)
                val shouldShowToolbar = totalVerticalOffset >= totalScrollRange
                if (isToolbarShown != shouldShowToolbar) {
                    isToolbarShown = shouldShowToolbar
                    appbar.isActivated = shouldShowToolbar
                    toolbarLayout.isTitleEnabled = shouldShowToolbar
                    if (isToolbarShown) {
                        Common.setStatusColorDark(requireActivity())
                    } else {
                        Common.setStatusColorLight(requireActivity())
                    }
                }

                //toolbarLayout.setStatusBarScrimColor(color)
            })
            homePhotosList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
//        setHasOptionsMenu(true)
        Common.setStatusColorLight(requireActivity())
        return binding.root
    }

    fun changeAlpha(color: Int, fraction: Float): Int {
        val red: Int = Color.red(color)
        val green: Int = Color.green(color)
        val blue: Int = Color.blue(color)
        val alpha = (Color.alpha(color) * fraction)
        return Color.argb(alpha.toInt(), red, green, blue)
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

    @Override
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(isToolbarShownKey, isToolbarShown)
        super.onSaveInstanceState(savedInstanceState)
    }

}