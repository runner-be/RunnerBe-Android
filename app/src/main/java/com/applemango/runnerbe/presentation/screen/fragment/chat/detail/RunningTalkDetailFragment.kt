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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentRunningTalkDetailBinding
import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.presentation.component.PaceComponentMini
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewHorizontalItemDeco
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemDialog
import com.applemango.runnerbe.presentation.screen.dialog.selectitem.SelectItemParameter
import com.applemango.runnerbe.presentation.screen.dialog.twobutton.TwoButtonDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.ImageBaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.chat.RunningTalkDetailClickListener
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.preview.RunningTalkDetailImageAdapter
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.preview.RunningTalkDetailImageSelectListener
import com.applemango.runnerbe.presentation.state.UiState
import com.applemango.runnerbe.util.LogUtil
import com.applemango.runnerbe.util.toUri
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class RunningTalkDetailFragment :
    ImageBaseFragment<FragmentRunningTalkDetailBinding>(R.layout.fragment_running_talk_detail) {

    private val viewModel: RunningTalkDetailViewModel by viewModels()
    private val args: RunningTalkDetailFragmentArgs by navArgs()

    @Inject
    lateinit var talkDetailListAdapter: RunningTalkDetailListAdapter
    @Inject
    lateinit var runningTalkDetailImageAdapter: RunningTalkDetailImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.fragment = this
        viewModel.roomId = args.roomId
        viewModel.roomRepName = args.roomRepUserName
        observeBind()
        initTalkDetailRecyclerView()
        initTalkAttachedImageRecyclerView()
        refresh()

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isDeclaration.value) viewModel.isDeclaration.value = false
                else navPopStack()
            }
        })

        binding.topMessageLayout.setOnClickListener { hideKeyBoard() }
        setupTalkDetail()
        setupTalkAttachedImages()
        setupTalkUpdate()
    }

    private fun setupTalkUpdate() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                refresh()
            }
        }
    }

    private fun setupTalkDetail() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.talkList.collectLatest { messages ->
                    talkDetailListAdapter.submitList(messages) {
                        binding.rcvMessage.scrollToPosition(messages.size - 1)
                    }
                }
            }
        }
    }

    private fun setupTalkAttachedImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.attachImageUrls.collectLatest { attachedImages ->
                    runningTalkDetailImageAdapter.submitList(attachedImages)
                }
            }
        }
    }

    private fun initTalkAttachedImageRecyclerView() {
        binding.rcvAttachedImage.apply {
            adapter = runningTalkDetailImageAdapter.apply {
                setRunningDetailImageListener(object: RunningTalkDetailImageSelectListener {
                    override fun imageDeleteClick(position: Int) {
                        val prevAttachedUrls = viewModel.attachImageUrls.value
                        viewModel.attachImageUrls.value = ArrayList(prevAttachedUrls).apply {
                            this.removeAt(position)
                        }
                    }
                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = null
            addItemDecoration(RecyclerViewHorizontalItemDeco(context, 8))
        }
    }

    private fun initTalkDetailRecyclerView() {
        binding.rcvMessage.apply {
            adapter = talkDetailListAdapter.apply {
                setTalkDetailClickListener(object : RunningTalkDetailClickListener {
                    override fun imageClicked(
                        imageUrl: String,
                        talkIdList: List<Int>,
                        clickItemId: Int
                    ) {
                        val images =
                            viewModel.messageList.filter { talkIdList.contains(it.messageId) && it.imageUrl != null }
                        val i = images.indexOfFirst { it.messageId == clickItemId }
                        val index = if (i < 0) 0 else i
                        navigate(
                            RunningTalkDetailFragmentDirections.moveToImageDetailFragment(
                                pageNumber = index,
                                images = images.mapNotNull { it.imageUrl }.toTypedArray(),
                                title = images[index].nickName
                            )
                        )
                    }
                })
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = null
            addItemDecoration(RecyclerViewItemDeco(context, 16))
        }
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
                            Log.e(this.javaClass.name, "observeBind - when - else - UiState | $it")
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
        viewModel.getDetailData(true)
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
                firstButtonText = resources.getString(R.string.no),
                secondButtonText = resources.getString(R.string.yes),
                firstEvent = {
                },
                secondEvent = {
                    viewModel.messageReport()
                }
            )
        }
    }
}