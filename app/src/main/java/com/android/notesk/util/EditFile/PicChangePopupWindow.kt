package com.android.notesk.util.EditFile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import com.android.notesk.R
import com.android.notesk.Model.Model
import com.android.notesk.util.EditFile.EditFileActivity.Companion.isChangePic
import com.android.notesk.util.EditFile.EditFileActivity.Companion.isPicExists

class PicChangePopupWindow(mContext: Context, addPic : ImageView) : PopupWindow(), View.OnClickListener {
    val mContext = mContext
    var view: View
    var addPic = addPic
    var pickBtn: Button
    var delBtn: Button

    init {
        view = LayoutInflater.from(mContext).inflate(R.layout.popup_window_pic_change, null)
        pickBtn = view.findViewById(R.id.pickBtn)
        delBtn = view.findViewById(R.id.delBtn)
        if (!isPicExists) {
            delBtn!!.visibility = View.INVISIBLE // 隱藏
//            picBtn!!.setBackgroundDrawable(getResources().getDrawable(R.drawable.radius3))
            pickBtn!!.setBackgroundResource(R.drawable.radius3)
        }
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

        pickBtn.setOnClickListener(this)
        delBtn.setOnClickListener(this)
    }

    fun pickBtnClicked() {
        val pick = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(mContext as Activity,pick, Model.IMAGE_REQUEST_CODE,null)
        dismiss()
    }

    fun delBtnClicked() {
        isChangePic = true
        isPicExists = false
        addPic.setImageDrawable(mContext.getDrawable(R.drawable.button_non_image))
        dismiss()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.pickBtn -> {
                pickBtnClicked()
            }R.id.delBtn -> {
                delBtnClicked()
            }
        }
    }

}