package com.applemango.runnerbe.presentation.screen.fragment.chat.detail

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentRunningTalkDetailBinding
import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.presentation.component.PaceComponentMini
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.screen.dialog.twobutton.TwoButtonDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.ImageBaseFragment
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.toUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class RunningTalkDetailFragment :
    ImageBaseFragment<FragmentRunningTalkDetailBinding>(R.layout.fragment_running_talk_detail) {

    private val viewModel: RunningTalkDetailViewModel by viewModels()
    private val args: RunningTalkDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.fragment = this
        viewModel.roomId = args.roomId
        viewModel.roomRepName = args.roomRepUserName
        context?.let {
            binding.messageRecyclerView.addItemDecoration(RecyclerViewItemDeco(it, 24))
        }
        refresh()
        observeBind()

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isDeclaration.value) viewModel.isDeclaration.value = false
                else navPopStack()
            }
        })

        binding.topMessageLayout.setOnClickListener { hideKeyBoard() }
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.actions.collect(::handleAction)
            }
            launch {
                viewModel.messageSendUiState.collect {
                    context?.let { context ->
                        if (it is UiState.Loading) showLoadingDialog(context)
                        else dismissLoadingDialog()
                    }
                    when (it) {
                        is UiState.Success -> refresh()
                        else -> { Log.e(this.javaClass.name, "observeBind - when - else - UiState") }
                    }
                }
            }
            launch {
                viewModel.roomInfo.collect {
                    binding.paceView.setContent {
                        PaceComponentMini(pace = Pace.getPaceByName(viewModel.roomInfo.value.pace)?:Pace.BEGINNER)
                    }
                }
            }
            launch {
                viewModel.messageReportUiState.collect {
                    context?.let { context ->
                        if (it is UiState.Loading) showLoadingDialog(context)
                        else dismissLoadingDialog()
                    }
                    when (it) {
                        is UiState.Success -> {
                            viewModel.isDeclaration.value = false
                            Toast.makeText(
                                context,
                                resources.getString(R.string.report_complete),
                                Toast.LENGTH_SHORT
                            ).show()
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

                        else -> {
                            Log.e(this.javaClass.name, "observeBind - when - else - UiState")
                        }
                    }
                }
            }
        }
    }

    override fun resultCameraCapture(image: File) {
        super.resultCameraCapture(image)
        context?.let {
            viewModel.selectImage(image.toUri(it))
        }
    }

    override fun resultImageSelect(dataList: ArrayList<Uri>) {
        super.resultImageSelect(dataList)
        dataList.forEach { viewModel.selectImage(it) }
    }

    private fun refresh() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getDetailData(true)
        }
    }

    private fun handleAction(action: RunningTalkDetailAction) {
        when(action) {
            is RunningTalkDetailAction.ShowImageSelect -> {
                checkAdditionalUserInfo {
                    context?.let {
                        SelectItemDialog.createShow(it, listOf(
                            SelectItemParameter("촬영하기") {
                                isImage = false
                                permReqLauncher.launch(Manifest.permission.CAMERA)
                            },
                            SelectItemParameter("앨범에서 선택하기") {
                                isImage = true
                                permReqLauncher.launch(
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                        Manifest.permission.READ_MEDIA_IMAGES
                                    else Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            }
                        ))
                    }
                }
            }
            is RunningTalkDetailAction.ShowToast -> {
                Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
            }
            is RunningTalkDetailAction.MoveToImageDetail -> {
                navigate(RunningTalkDetailFragmentDirections.moveToImageDetailFragment(
                    title = action.title,
                    images = action.images.toTypedArray(),
                    pageNumber = action.clickPageNumber
                ))
            }
        }
    }

    fun showReportDialog() {
        context?.let {
            TwoButtonDialog.createShow(
                it,
                title = resources.getString(R.string.msg_warning_report),
                firstButtonText = resources.getString(R.string.yes),
                secondButtonText = resources.getString(R.string.no),
                firstEvent = {
                    viewModel.messageReport()
                },
                secondEvent = {}
            )
        }
    }
}