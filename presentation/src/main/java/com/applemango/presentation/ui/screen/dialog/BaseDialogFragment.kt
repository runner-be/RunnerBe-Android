package com.applemango.presentation.ui.screen.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.applemango.presentation.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseDialogFragment<out VB : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : DialogFragment() {
    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    private var _compositeDisposable: CompositeDisposable? = null
    protected val compositeDisposable: CompositeDisposable get() = _compositeDisposable!!

    @Inject
    protected lateinit var navController: NavController

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.confirmDialogStyle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        _compositeDisposable = CompositeDisposable()
        return binding.root
    }

    override fun onDestroyView() {
        _compositeDisposable = null
        _binding = null
        super.onDestroyView()
    }

    protected fun navigateTo(directions: NavDirections) {
        navController.navigate(directions)
    }

    protected fun popBackStack() {
        navController.popBackStack()
    }
}