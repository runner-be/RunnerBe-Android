package com.applemango.runnerbe.presentation.screen.fragment.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentWebViewBinding
import kotlinx.coroutines.launch

class WebViewFragment : BaseFragment<FragmentWebViewBinding>(R.layout.fragment_web_view) {

    private val webViewViewModel: WebViewViewModel by viewModels()
    private val args: WebViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = webViewViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            webViewViewModel.actions.collect {
                when(it) {
                    is WebViewAction.MoveToBack -> { navPopStack() }
                }
            }
        }
        webViewViewModel.title.value = args.title
        binding.webView.apply {

            settings.javaScriptEnabled = true
            settings.setSupportMultipleWindows(false) // 새창띄우기 허용여부
            settings.javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창뛰우기 (멀티뷰) 허용여부
            settings.loadWithOverviewMode = true //메타태크 허용여부
            settings.useWideViewPort = true //화면 사이즈 맞추기 허용여부
            settings.setSupportZoom(true) //화면 줌 허용여부
            settings.builtInZoomControls = true //화면 확대 축소 허용여부

            loadUrl(args.url)
        }

    }
}