package com.android.notesk.util.View

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity
import com.android.notesk.R
import com.android.notesk.util.Presenter.ChooseFilePresenter
import com.android.notesk.util.Presenter.SyncPresenter


class SyncPopupWindow(mContext: Context,presenter : ChooseFilePresenter) : PopupWindow(), View.OnClickListener {
    var view: View
    var uploadBtn: Button
    var updatedelBtn: Button
    var presenter = presenter

    init {
        view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_sync, null)
        uploadBtn = view.findViewById(R.id.upload)
        updatedelBtn = view.findViewById(R.id.update)
        // 外部可點擊
        this.isOutsideTouchable = true
        // mMenuView添加OnTouchListener監聽判斷獲取觸屏位置如果在選擇框外面則銷毀彈出框
        view.setOnTouchListener { v, event ->
            val height = view.findViewById<View>(R.id.popLayout).top
            val y = event.y.toInt()
            if (event.action == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss()
                }
            }
            true
        }
        this.contentView = view
        // 窗口高和寬填滿
        this.height = RelativeLayout.LayoutParams.MATCH_PARENT
        this.width = RelativeLayout.LayoutParams.MATCH_PARENT
        // 設置彈出窗體可點擊
        this.isFocusable = true
        // 背景色
        val dw = ColorDrawable(-0x50000000)
        setBackgroundDrawable(dw)
        // 彈出窗体的動畫
        this.animationStyle = R.style.take_photo_anim

        uploadBtn.setOnClickListener(this)
        updatedelBtn.setOnClickListener(this)
    }

    fun uploadClicked() {
//        val pdfUri: Uri = Uri.parse("file://sdcard/sdcard0/test.pdf")
//        val shareIntent = ShareCompat.IntentBuilder.from(mContext as ChooseFileActivity)
//            .setText("Share PDF doc")
//            .setType("application/pdf")
//            .setStream(pdfUri)
//            .intent
//            .setPackage("com.google.android.apps.docs")
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        mContext.applicationContext.startActivity(shareIntent)
    }

    fun updateClicked() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.upload -> {
                presenter.syncClick()
                dismiss()
//                uploadClicked()
            }R.id.update -> {
            updateClicked()
            dismiss()
            }
        }
    }

}