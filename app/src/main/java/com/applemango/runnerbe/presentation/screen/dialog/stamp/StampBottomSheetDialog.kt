package com.applemango.runnerbe.presentation.screen.dialog.stamp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
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
    private var onStampConfirmListener: OnStampConfirmListener? = null

    init {
        this.selectedStamp = selectedStamp
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStampRecyclerView()
        initClickListeners()
        initStampInfo()
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

    private fun initStampRecyclerView() {
        with(binding.rcvStamp) {
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            _stampAdapter = StampAdapter()
            adapter = stampAdapter.apply {
                submitList(initStampItems())
                setSelectedStamp(selectedStamp)
                setOnStampClickListener { stamp ->
                    selectedStamp = stamp
                    binding.tvStampName.text = stamp.name
                    binding.tvStampDetail.text = stamp.description
                }
            }
            layoutManager = linearLayoutManager
            val space = 24.dpToPx(context)
            addItemDecoration(LeftSpaceItemDecoration(space))
        }
    }

    companion object {
        fun createAndShow(
            fragmentManager: FragmentManager,
            selectedStamp: StampItem,
            onStampConfirmListener: OnStampConfirmListener
        ) {
            val bottomSheetDialog = StampBottomSheetDialog(selectedStamp)
            with(bottomSheetDialog) {
                this.onStampConfirmListener = onStampConfirmListener
                show(fragmentManager, tag)
            }
        }
    }
}