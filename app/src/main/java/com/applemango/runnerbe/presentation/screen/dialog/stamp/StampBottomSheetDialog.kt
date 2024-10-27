package com.applemango.runnerbe.presentation.screen.dialog.stamp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogBottomSheetStampBinding
import com.applemango.runnerbe.presentation.screen.dialog.CustomBottomSheetDialog
import com.applemango.runnerbe.util.dpToPx
import com.applemango.runnerbe.util.recyclerview.LeftSpaceItemDecoration

class StampBottomSheetDialog(
    selectedStamp: StampItem
): CustomBottomSheetDialog<DialogBottomSheetStampBinding>(R.layout.dialog_bottom_sheet_stamp) {
    private var _stampAdapter: StampAdapter? = null
    private val stampAdapter get() = _stampAdapter!!

    private var selectedStamp: StampItem
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
        with(binding) {
            tvStampName.text = selectedStamp.name
            tvStampDetail.text = selectedStamp.description
        }
    }

    private fun initClickListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                onStampConfirmListener?.onConfirmClicked(selectedStamp)
                dismiss()
            }
        }
    }

    private fun scrollToSelectedStamp(
        layoutManager: LinearLayoutManager,
        stamp: StampItem
    ) {
        val stampList = initStampItems()
        smoothScroller.targetPosition = stampList.indexOf(stamp)
        layoutManager.startSmoothScroll(smoothScroller)
    }

    private fun initStampRecyclerView() {
        with(binding.rcvStamp) {
            _stampAdapter = StampAdapter().apply {
                setIsPersonalLog(isPersonalLog)
            }
            adapter = stampAdapter.apply {
                submitList(initStampItems())
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
            selectedStamp: StampItem,
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