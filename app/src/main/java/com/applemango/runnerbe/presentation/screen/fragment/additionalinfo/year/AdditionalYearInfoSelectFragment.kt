package com.applemango.runnerbe.presentation.screen.fragment.additionalinfo.year

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentAdditionalYearInfoSelectBinding
import com.applemango.runnerbe.presentation.screen.fragment.additionalinfo.AdditionalInfoViewModel
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener
import com.github.gzuliyujiang.wheelview.widget.WheelView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdditionalYearInfoSelectFragment: BaseFragment<FragmentAdditionalYearInfoSelectBinding>(R.layout.fragment_additional_year_info_select) {

    private val infoViewModel : AdditionalInfoViewModel by activityViewModels()
    private val viewModel : AdditionalYearInfoSelectViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        setWheelYear()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actions.collect {
                when(it) {
                    is AdditionalYearInfoSelectAction.MoveToBack -> { navPopStack() }
                    is AdditionalYearInfoSelectAction.ActivityFinish -> { activity?.finish() }
                    is AdditionalYearInfoSelectAction.MoveToNext -> {
                        infoViewModel.yearOfBrith = binding.yearWheelView.getCurrentItem()
                        infoViewModel.yearOfBrith?.let {
                            navigate(AdditionalYearInfoSelectFragmentDirections.actionYearInfoFragmentToAdditionalGenderSelectFragment())
                        }
                    }
                }
            }
        }
    }

    private fun setWheelYear() {
        val yearList = viewModel.yearList
        binding.yearWheelView.apply {
            setOnWheelChangedListener(object : OnWheelChangedListener {
                override fun onWheelScrolled(view: WheelView?, offset: Int) {}

                override fun onWheelSelected(view: WheelView?, position: Int) {
                    viewModel.selectYear(yearList[position])
                }

                override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {}

                override fun onWheelLoopFinished(view: WheelView?) {}

            })
            setData(yearList, yearList.indexOf(infoViewModel.yearOfBrith?:yearList[yearList.size/2]))
            viewModel.selectYear(getCurrentItem())
        }
    }
}