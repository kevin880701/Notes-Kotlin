package com.lhr.notes_kotlin.util.manager

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lhr.notes_kotlin.R
import com.lhr.notes_kotlin.databinding.DialogDeleteFolderBinding
import java.util.Objects

class DialogManager {

    companion object {
        private var dialog: AlertDialog? = null

        fun showTicketDialog(
            activity: AppCompatActivity,
        ) {
            activity.runOnUiThread {
                val builder = AlertDialog.Builder(activity)
                builder.setCancelable(false)
                val binding: DialogDeleteFolderBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(activity), R.layout.dialog_delete_folder, null, false
                )
                binding.textMessage.setOnClickListener(View.OnClickListener { dialog?.dismiss() })
                builder.setView(binding.root)
                dialog = builder.create()
                dialog?.setCancelable(false)
                Objects.requireNonNull<Window>(dialog?.window)
                    .setBackgroundDrawableResource(R.color.transparent)
                dialog?.show()

                val layoutParams = dialog?.window?.attributes
                layoutParams?.width = 700 // 设置新的宽度，单位是像素
                dialog?.window?.attributes = layoutParams

            }
        }
    }
}