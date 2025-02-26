package com.applemango.presentation.ui.screen.dialog

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.applemango.presentation.ui.screen.activity.AdditionalInfoActivity
import com.applemango.presentation.util.CachingObject
import com.applemango.presentation.R
import com.applemango.presentation.databinding.DialogNoAdditionalInfoBinding

class NoAdditionalInfoDialog: CustomBottomSheetDialog<DialogNoAdditionalInfoBinding>(R.layout.dialog_no_additional_info){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noButton.setOnClickListener{
            CachingObject.isColdStart = false
            dismiss()
        }
        binding.infoInputButton.setOnClickListener{
            context?.startActivity(Intent(context, AdditionalInfoActivity::class.java))
            dismiss()
        }
    }
}