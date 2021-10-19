package id.co.app.core.customview.listbottomsheet

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import id.co.app.core.R
import id.co.app.core.base.adapterdelegate.CompositeAdapter
import id.co.app.core.base.adapterdelegate.DelegateAdapterItem
import id.co.app.core.base.adapterdelegate.DelegateAdapterItemClick
import id.co.app.core.databinding.BottomSheetListBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * Created by Lukas Kristianto on 5/26/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class ListBottomSheet(
    private val title: String,
    private val items: List<ListModel>,
    private val listener: DelegateAdapterItemClick,
) : BottomSheetDialogFragment(), DelegateAdapterItemClick {
    private val binding by lazy { BottomSheetListBinding.inflate(layoutInflater) }
    private val newGrids = mutableListOf<ListModel>()
    private val adapter by lazy {
        CompositeAdapter.Builder()
            .add(ListItemAdapter(this))
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newGrids.addAll(items)
        setupView()
    }

    override fun getTheme() = R.style.RoundedBottomSheetDialog

    override fun onClick(item: DelegateAdapterItem) {
        if (item is ListModel) {
            listener.onClick(item)
            this.dismiss()
        }
    }

    private fun setupView() {
        setupDialog()
        binding.titleGridInfo.text = title
        binding.recyclerView.adapter = adapter
        splitListWhenNeeded()

        binding.closeButton.setOnClickListener {
            this.dismiss()
        }
    }

    private fun splitListWhenNeeded() {
        if (items.size > 10) {
            items.withIndex().groupBy { it.index / 10 }
                .map {
                    it.value.map { it.value }
                }.forEach {
                    if (adapter.itemCount == 0) {
                        adapter.submitList(it)
                    } else {
                        Handler().postDelayed({
                            adapter.submitList(adapter.currentList + it)
                        }, 300)
                    }
                }
        } else {
            adapter.submitList(items)
        }
    }

    private fun setupDialog() {
        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }
}