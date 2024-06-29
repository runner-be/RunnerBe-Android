package com.applemango.runnerbe.presentation.screen.dialog.appliedrunner

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.RunnerBeApplication
import com.applemango.runnerbe.databinding.DialogWaitingRunnuerListBinding
import com.applemango.runnerbe.presentation.model.listener.PostDialogListener
import com.applemango.runnerbe.presentation.screen.dialog.CustomBottomSheetDialog
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.twobutton.TwoButtonDialog
import com.applemango.runnerbe.presentation.screen.fragment.main.postdetail.PostDetailViewModel
import com.applemango.runnerbe.presentation.state.UiState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WaitingRunnerListDialog(
    private val detailViewModel: PostDetailViewModel,
    private val postListener: PostDialogListener,
    private val roomId: Int?
) : CustomBottomSheetDialog<DialogWaitingRunnuerListBinding>(R.layout.dialog_waiting_runnuer_list) {

    private val viewModel: WaitingRunnerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.detailViewModel = detailViewModel
        binding.dialog = this
        if(detailViewModel.post.value != null) viewModel.post =detailViewModel.post.value!!
        else dismiss()
        viewModel.waitingInfo.addAll(detailViewModel.waitingInfo)
        observeBind()
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.acceptUiState.collect {
                context?.let { context ->
                    if (it is UiState.Loading) showLoadingDialog(context)
                    else dismissLoadingDialog()
                }
                when(it) {
                    is UiState.Success -> {
                        detailViewModel.getPostDetail(detailViewModel.post.value!!.postId, RunnerBeApplication.mTokenPreference.getUserId())
                    }
                    is UiState.Failed -> {
                        context?.let { context ->
                            MessageDialog.createShow(
                                context = context,
                                message = it.message,
                                buttonText = resources.getString(R.string.confirm)
                            )
                        }
                    }
                }
            }
        }
    }

    fun moveToMessage() {
        roomId?.let {
            postListener.moveToMessage(it, viewModel.post?.nickName)
        }
    }

    fun clickBottomButton() {
        context?.let {
            TwoButtonDialog.createShow(
                context = it,
                title = resources.getString(if (detailViewModel.isMyPost()) R.string.question_post_close else R.string.question_post_apply),
                firstButtonText = resources.getString(R.string.no),
                secondButtonText = resources.getString(R.string.yes),
                firstEvent = {},
                secondEvent = {
                    detailViewModel.bottomProcess()
                }
            )
        }
    }

    fun goBack() { dismiss() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val sheetDialog = super.onCreateDialog(savedInstanceState)
        sheetDialog.setOnShowListener {
            val bottomSheet =
                sheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val params = bottomSheet.layoutParams
            bottomSheet.setBackgroundResource(android.R.color.transparent)
            params.height = getScreenHeight()
            bottomSheet.layoutParams = params
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).isDraggable = true
        }
        return sheetDialog
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}