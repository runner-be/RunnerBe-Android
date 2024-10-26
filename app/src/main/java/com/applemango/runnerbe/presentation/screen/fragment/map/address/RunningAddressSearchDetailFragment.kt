package com.applemango.runnerbe.presentation.screen.fragment.map.address

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentRunningAddressSearchDetailBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.detail.ImageDetailFragmentArgs
import com.applemango.runnerbe.presentation.screen.fragment.map.write.AddressData
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RunningAddressSearchDetailFragment :
    BaseFragment<FragmentRunningAddressSearchDetailBinding>(R.layout.fragment_running_address_search_detail) {

    private val paramAddress: RunningAddressSearchDetailFragmentArgs by navArgs()
    private val viewModel: RunningAddressSearchDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvAddressMain.text = paramAddress.address.placeName
            tvAddressSub.text = paramAddress.address.roadAddress
        }
        initListeners()
    }

    private fun initListeners() {
        compositeDisposable.addAll(
            binding.backBtn.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    activity?.finish()
                },
            binding.tvAddressSubEdit.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    navPopStack()
                },
            binding.btnRegister.clicks()
                .throttleFirst(1000L, TimeUnit.MILLISECONDS)
                .subscribe {
                    val address = AddressData(
                        paramAddress.address.placeName,
                        paramAddress.address.roadAddress,
                        binding.tieAddressDetail.text.toString(),
                        paramAddress.address.x,
                        paramAddress.address.y,
                    )
                    val resultIntent = Intent().apply {
                        putExtra("address", address)
                    }
                    activity?.setResult(RESULT_OK, resultIntent)
                    activity?.finish()
                }
        )
    }
}