package com.applemango.runnerbe.presentation.screen.dialog.menu

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.DialogEditDeleteBinding

class MenuDialog(
    context: Context,
    userId: Int,
    logId: Int
) : AlertDialog(context, R.style.confirmDialogStyle) {
    private val userId: Int
    private val logId: Int

    private var onEditClickListener: OnEditClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    val binding : DialogEditDeleteBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_edit_delete,
            null,
            false
        )
    }

    init {
        this.userId = userId
        this.logId = logId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            btnEdit.setOnClickListener {
                onEditClickListener?.onEditClicked(logId)
                dismiss()
            }
            btnDelete.setOnClickListener {
                onDeleteClickListener?.onDeleteClick(userId, logId)
                dismiss()
            }
        }
    }

    companion object {
        fun createAndShow(
            context: Context,
            userId: Int,
            logId: Int,
            editClickListener: OnEditClickListener,
            deleteClickListener: OnDeleteClickListener
        ) {
            val dialog = MenuDialog(context, userId, logId)
            with(dialog) {
                this.onEditClickListener = editClickListener
                this.onDeleteClickListener = deleteClickListener
                show()
            }
        }
    }
}