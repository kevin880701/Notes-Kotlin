package com.android.notesk.util.ChooseFile.BackupChoose

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.android.notesk.Model.Model.Companion.OVER_FILE
import com.android.notesk.Model.Model.Companion.KEEP_FILE
import com.android.notesk.R
import com.android.notesk.util.ChooseFile.ChooseFilePresenter


class BackupChoosePopupWindow(mContext: Context, presenter : ChooseFilePresenter) : PopupWindow(), View.OnClickListener {
    var view: View
    var btnOverFile: Button
    var btnKeepFile: Button
    var presenter = presenter

    init {
        view = LayoutInflater.from(mContext).inflate(R.layout.popup_window_backup_choose, null)
        btnOverFile = view.findViewById(R.id.overFile)
        btnKeepFile = view.findViewById(R.id.keepFile)
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

        btnOverFile.setOnClickListener(this)
        btnKeepFile.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.overFile -> {
                presenter.chooseBackup(OVER_FILE)
                dismiss()
            }R.id.keepFile -> {
                presenter.chooseBackup(KEEP_FILE)
                dismiss()
            }
        }
    }

}