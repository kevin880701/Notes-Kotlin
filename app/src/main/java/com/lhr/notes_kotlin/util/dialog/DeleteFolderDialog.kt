package com.lhr.notes_kotlin.util.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.lhr.notes_kotlin.R
import com.lhr.notes_kotlin.databinding.DialogDeleteFolderBinding
import com.lhr.notes_kotlin.util.dpToPixel

class DeleteFolderDialog(
    listener: Listener
) : DialogFragment() {

    private var dialog: AlertDialog? = null
    private var listener = listener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding: DialogDeleteFolderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.dialog_delete_folder,
            null,
            false
        )
        val builder = AlertDialog.Builder(activity)
        binding.textConfirm.setOnClickListener {
            listener.onConfirmClick()
            dismiss()
        }
        binding.textCancel.setOnClickListener {
            dismiss()
        }
        dialog = builder.setView(binding.root).create()
        // 設置背景顏色
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.transparent)))

        return dialog!!
    }

    override fun onResume() {
        super.onResume()

        val params = getDialog()?.window?.attributes
        params?.width = dpToPixel(requireContext(),200) // 設置寬度為200像素
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT // 高度自適應內容
        getDialog()?.window?.attributes = params
    }

    fun initView(binding: DialogDeleteFolderBinding) {

    }

    interface Listener {
        fun onConfirmClick()
    }
}