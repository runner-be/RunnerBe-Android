package com.applemango.runnerbe.presentation.screen.fragment.additionalinfo.complete

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentAdiitionalRegisterCompleteBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment

class RegisterCompleteFragment :BaseFragment<FragmentAdiitionalRegisterCompleteBinding>(R.layout.fragment_adiitional_register_complete) {

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