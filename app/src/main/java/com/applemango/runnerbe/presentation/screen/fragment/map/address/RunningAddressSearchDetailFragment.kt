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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunningAddressSearchDetailFragment :
    BaseFragment<FragmentRunningAddressSearchDetailBinding>(R.layout.fragment_running_address_search_detail),
    View.OnClickListener {

    private val paramAddress: RunningAddressSearchDetailFragmentArgs by navArgs()
    private val viewModel: RunningAddressSearchDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnRegister.setOnClickListener(this@RunningAddressSearchDetailFragment)
            backBtn.setOnClickListener(this@RunningAddressSearchDetailFragment)
            tvAddressSubEdit.setOnClickListener(this@RunningAddressSearchDetailFragment)
            tvAddressMain.text = paramAddress.address.placeName
            tvAddressSub.text = paramAddress.address.roadAddress
//            tieAddressDetail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//                if (hasFocus) {
//                    tieAddressDetail.hint = getString(R.string.running_location_search_detail_hint_1)
//                } else {
//                    tieAddressDetail.hint = getString(R.string.running_location_search_detail_hint_2)
//                }
//            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtn, binding.tvAddressSubEdit -> {
                Log.e("navPopStack", findNavController().currentBackStackEntry.toString())
                navPopStack()
            }

            binding.btnRegister -> {
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
        }
    }
}