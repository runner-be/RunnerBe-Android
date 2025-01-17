package com.applemango.runnerbe.presentation.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.res.ResourcesCompat
import com.applemango.runnerbe.R

class CustomRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : AppCompatRadioButton(context, attrs) {

    init {
        updateFont(isChecked)
        updateTextColor(isChecked)
        setBackgroundResource(R.drawable.selector_custom_radio)
        setOnCheckedChangeListener { buttonView, isChecked ->
            updateFont(isChecked)
            updateTextColor(isChecked)
        }
        buttonDrawable = null
    }

    private fun updateFont(isChecked: Boolean) {
        typeface = ResourcesCompat.getFont(context, if(isChecked) R.font.pretendard_semi_bold else R.font.pretendard_regular)
    }

    private fun updateTextColor(isChecked: Boolean) {
        setTextColor(ResourcesCompat.getColor(context.resources, if(isChecked) R.color.dark_g6 else R.color.dark_g3, null))
    }
}