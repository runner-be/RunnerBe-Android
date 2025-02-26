package com.applemango.presentation.ui.screen.dialog.stamp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.applemango.presentation.ui.screen.dialog.CustomBottomSheetDialog
import com.applemango.presentation.util.ToastUtil
import com.applemango.presentation.util.dpToPx
import com.applemango.presentation.util.recyclerview.LeftSpaceItemDecoration
import com.applemango.presentation.R
import com.applemango.presentation.databinding.DialogBottomSheetStampBinding

class StampBottomSheetDialog(
    selectedStamp: StampItem?
): CustomBottomSheetDialog<DialogBottomSheetStampBinding>(R.layout.dialog_bottom_sheet_stamp) {
    private var _stampAdapter: StampAdapter? = null
    private val stampAdapter get() = _stampAdapter!!

    private var selectedStamp: StampItem?
    private var isPersonalLog: Boolean = true
    private var onStampConfirmListener: OnStampConfirmListener? = null

    private val stampLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private val smoothScroller: SmoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getHorizontalSnapPreference(): Int = SNAP_TO_START
        }
    }

    init {
        this.selectedStamp = selectedStamp
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStampRecyclerView()
        initClickListeners()
        initStampInfo()
        scrollToSelectedStamp(stampLayoutManager, selectedStamp)
    }

    override fun onDestroyView() {
        _stampAdapter = null
        super.onDestroyView()
    }

    private fun initStampInfo() {
        val selectedStamp = selectedStamp ?: StampItem(
            "RUN001",
            R.drawable.ic_stamp_1_personal,
            requireContext().getString(R.string.stamp_1_name),
            requireContext().getString(R.string.stamp_1_description),
            true
        )
        with(binding) {
            tvStampName.text = selectedStamp.name
            tvStampDetail.text = selectedStamp.description
        }
    }

    private fun initClickListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                selectedStamp?.let {
                    onStampConfirmListener?.onConfirmClicked(it)
                    dismiss()
                } ?: ToastUtil.showShortToast(requireContext(), "스탬프를 선택해주세요")
            }
        }
    }

    private fun scrollToSelectedStamp(
        layoutManager: LinearLayoutManager,
        stamp: StampItem?
    ) {
        if (stamp != null) {
            val stampList: List<StampItem> = context?.let {
                StampItem.getStampItems(it)
            }?: emptyList()
            if (stampList.isNotEmpty()) {
                smoothScroller.targetPosition = stampList.indexOf(stamp)
                layoutManager.startSmoothScroll(smoothScroller)
            }
        }
    }

    private fun initStampRecyclerView() {
        with(binding.rcvStamp) {
            _stampAdapter = StampAdapter().apply {
                setIsPersonalLog(isPersonalLog)
            }
            adapter = stampAdapter.apply {
                submitList(StampItem.getStampItems(context))
                setSelectedStamp(selectedStamp)
                setOnStampClickListener { stamp ->
                    selectedStamp = stamp
                    binding.tvStampName.text = stamp.name
                    binding.tvStampDetail.text = stamp.description
                }
            }
            layoutManager = stampLayoutManager
            val space = 24.dpToPx(context)
            addItemDecoration(LeftSpaceItemDecoration(space))
        }
    }

    companion object {
        fun createAndShow(
            fragmentManager: FragmentManager,
            selectedStamp: StampItem?,
            isPersonalLog: Boolean,
            onStampConfirmListener: OnStampConfirmListener
        ) {
            val bottomSheetDialog = StampBottomSheetDialog(selectedStamp)
            with(bottomSheetDialog) {
                this.onStampConfirmListener = onStampConfirmListener
                this.isPersonalLog = isPersonalLog
                show(fragmentManager, tag)
            }
        }
    }
}