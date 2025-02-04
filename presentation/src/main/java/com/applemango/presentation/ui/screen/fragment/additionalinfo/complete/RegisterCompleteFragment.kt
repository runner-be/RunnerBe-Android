package com.applemango.presentation.ui.screen.fragment.additionalinfo.complete

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.applemango.presentation.ui.screen.fragment.base.BaseFragment
import com.applemango.presentation.R
import com.applemango.presentation.databinding.FragmentAdiitionalRegisterCompleteBinding

class RegisterCompleteFragment :
    BaseFragment<FragmentAdiitionalRegisterCompleteBinding>(R.layout.fragment_adiitional_register_complete) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragment = this
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })
    }

    fun start() {
        activity?.finish()
    }
}