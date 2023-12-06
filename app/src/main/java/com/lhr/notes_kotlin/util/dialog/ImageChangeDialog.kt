package com.lhr.notes_kotlin.util.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.lhr.notes_kotlin.R
import com.lhr.notes_kotlin.databinding.DialogImageChangeBinding

class ImageChangeDialog(
    listener: Listener
) : DialogFragment() {

    private var dialog: AlertDialog? = null
    private var listener = listener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding: DialogImageChangeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.dialog_image_change,
            null,
            false
        )
        val builder = AlertDialog.Builder(activity)
//        builder.setCancelable(false)
        binding.textChangeImage.setOnClickListener {
            listener.onChangeImage()
            dismiss()
        }
        binding.textDeleteImage.setOnClickListener {
            listener.onDeleteImage()
            dismiss()
        }
        initView(binding)

        dialog = builder.setView(binding.root).create()

        dialog?.window?.attributes?.windowAnimations = R.style.ChangeImageDialog
        // 設置背景顏色
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.transparent)))

        // 設置Dialog的位置為底部
//        dialog?.window?.setGravity(Gravity.BOTTOM)
        val layoutParams = dialog?.window?.attributes
        layoutParams?.gravity = Gravity.BOTTOM
        layoutParams?.y = 100 // 设置底部间距
        dialog?.window?.attributes = layoutParams

        return dialog!!
    }

    fun initView(binding: DialogImageChangeBinding) {

    }

    interface Listener {
        fun onDeleteImage()
        fun onChangeImage()
    }
}