package com.applemango.runnerbe.presentation.screen.fragment.mypage.paceinfo

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentRegistPaceInfoBinding
import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.dialog.message.MessageDialog
import com.applemango.runnerbe.presentation.screen.dialog.message.YesNoButtonDialog
import com.applemango.runnerbe.presentation.screen.dialog.pace.OneButtonClickListener
import com.applemango.runnerbe.presentation.screen.dialog.pace.OneButtonImageTextData
import com.applemango.runnerbe.presentation.screen.dialog.pace.OneButtonImageTextDialog
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.state.UiState
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PaceInfoRegistFragment :
    BaseFragment<FragmentRegistPaceInfoBinding>(R.layout.fragment_regist_pace_info) {

    private val viewModel: PaceInfoViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.action.collect {
                when (it) {
                    is PaceInfoRegistAction.MoveToBack -> navPopStack()
                    is PaceInfoRegistAction.ShowCompleteDialog -> {
                        createCompleteDialog(it.pace, getString(it.titleResource, it.pace.time), getString(it.descriptionResource))
                    }
                }
            }
        }
        context?.let {
            binding.paceInfoRecyclerView.addItemDecoration(RecyclerViewItemDeco(it, 12))
        }
        viewModel.paceInfoUiState.observe(viewLifecycleOwner) {
            context?.let { context ->
                if (it is UiState.Loading) showLoadingDialog(context)
                else dismissLoadingDialog()
            }
            if(it is UiState.Failed) {
                context?.let { context ->
                    MessageDialog.createShow(
                        context = context,
                        message = it.message,
                        buttonText = resources.getString(R.string.confirm)
                    )
                }
            }
        }
        initBackPressDispatcher()
        initListener()
    }

    private fun initBackPressDispatcher() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showMessageDialog()
                }
            })
    }

    private fun showMessageDialog() {
        YesNoButtonDialog.createShow(
            requireContext(),
            getString(R.string.dialog_pace_exit),
            getString(R.string.yes),
            getString(R.string.no),
            positiveEvent = fun() {
                goBack()
            },
            negativeEvent = fun() {

            }
        )
    }

    private fun initListener() {
        compositeDisposable.addAll(
            binding.ivClose.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    showMessageDialog()
                }
        )
    }

    private fun createCompleteDialog(pace: Pace, title : String, description: String) {
        context?.let { context ->
            OneButtonImageTextDialog.createShow(
                context,
                OneButtonImageTextData(
                    imageResource = when (pace.key) {
                        Pace.BEGINNER.key -> R.drawable.ic_beginner_pace_48 //입문
                        Pace.AVERAGE.key -> R.drawable.ic_general_pace_48 //평균
                        Pace.HIGH.key -> R.drawable.ic_master_pace_48//고수
                        else -> R.drawable.ic_grand_master_pace_48 //초고수
                    },
                    title = title,
                    description = description,
                    buttonTitle = getString(R.string.confirm)
                ),
                object : OneButtonClickListener {
                    override fun onClicked() {
                        when(viewModel.inputPage) {
                            PaceRegistrationInputPage.MAP.mode -> {
                                findNavController().navigate(PaceInfoRegistFragmentDirections.moveToWriteFragment(null))
                            }
                            PaceRegistrationInputPage.MY_PAGE.mode -> {
                                refreshBack()
                            }
                        }

                    }
                }
            )
        }
    }

    private fun refreshBack() {
        requireActivity()
            .supportFragmentManager
            .setFragmentResult("refresh", bundleOf())
        navPopStack()
    }
}