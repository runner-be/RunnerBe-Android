package com.applemango.presentation.ui.screen.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.applemango.presentation.R
import com.applemango.presentation.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 로그인 이후에 사용되는 화면이 이 액티비티 내부에 작성합니다.
 */
@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    private var recentBackPressedTime: Long = 0L

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val currTime = System.currentTimeMillis()

            if (recentBackPressedTime + 2000 > currTime) {
                finish()
                return
            }

            Toast.makeText(this@HomeActivity, getString(R.string.toast_back_press_twice), Toast.LENGTH_SHORT).show()
            recentBackPressedTime = currTime
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onDestroy() {
        onBackPressedCallback.remove()
        if (isChangingConfigurations) {

        }
        super.onDestroy()
    }
}