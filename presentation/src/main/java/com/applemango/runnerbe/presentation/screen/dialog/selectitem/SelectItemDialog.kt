package com.applemango.runnerbe.presentation.screen.dialog.selectitem

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogSelectListBinding

class SelectItemDialog(context : Context) : AlertDialog(context, R.style.confirmDialogStyle) {
    val binding : DialogSelectListBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_select_list,
            null,
            false
        )
    }

    private val items : ObservableArrayList<SelectListData> = ObservableArrayList()

    companion object {
        fun createShow(
            context: Context,
            items : List<SelectItemParameter>
        ) {
            val dialog = SelectItemDialog(context)
            with(dialog) {
                this.items.addAll(items.map { data -> SelectListData(data.itemName) {
                    data.event()
                    dismiss()
                } })
            }
            dialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.items = items
        val deco = DividerItemDecoration(context, VERTICAL)
        ResourcesCompat.getDrawable(context.resources, R.drawable.bg_ractangle_stroke_g4_5, null)
            ?.let { deco.setDrawable(it) }
        binding.selectItemRecyclerview.addItemDecoration(deco)
    }
}