package com.lhr.notes_kotlin.viewmodel.implementations

import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.ViewModel
import com.lhr.notes_kotlin.R
import com.lhr.notes_kotlin.view.activity.LockActivity
import com.lhr.notes_kotlin.view.activity.MainActivity
import com.lhr.notes_kotlin.viewmodel.LockViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LockViewModelImpl: LockViewModel, ViewModel() {
    override var count: Int = 0
    override var realCount: Int = 0
    override fun real(lockActivity: LockActivity) {
        realCount++
        if (realCount == 3) {
            val intent = Intent(lockActivity, MainActivity::class.java)
            lockActivity.startActivity(intent)
            lockActivity.finish()
        }
    }

    override fun count(lockActivity: LockActivity, vibrator: Vibrator) {
        count++
        when (count) {
            1 -> {vibrator(vibrator)
                lockActivity.binding.imageViewPasswordCir1.setBackgroundResource(R.drawable.password_enter_cir)}
            2 -> {vibrator(vibrator)
                lockActivity.binding.imageViewPasswordCir2.setBackgroundResource(R.drawable.password_enter_cir)}
            3 -> {vibrator(vibrator)
                lockActivity.binding.imageViewPasswordCir3.setBackgroundResource(R.drawable.password_enter_cir)}
            4 -> {
                lockActivity.binding.imageViewPasswordCir4.setBackgroundResource(R.drawable.password_enter_cir)
                CoroutineScope(Dispatchers.Main).launch {
                    // 延遲100毫秒
                    delay(100)
                    lockActivity.binding.textViewEnterPassword.text = "密碼錯誤"
                    lockActivity.binding.imageViewPasswordCir1.setBackgroundResource(R.drawable.password_keyboard_cir)
                    lockActivity.binding.imageViewPasswordCir2.setBackgroundResource(R.drawable.password_keyboard_cir)
                    lockActivity.binding.imageViewPasswordCir3.setBackgroundResource(R.drawable.password_keyboard_cir)
                    lockActivity.binding.imageViewPasswordCir4.setBackgroundResource(R.drawable.password_keyboard_cir)
                    vibrator(vibrator)
                }
                count = 0
            }
        }
    }

    /**
     * 震動
     * @param vibrator 震動服務
     */
    override fun vibrator(vibrator: Vibrator) {
        // 檢查設備的版本是否支援新的震動效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 創建一個具有振動效果的震動
            val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            // 在舊版本的設備上執行傳統的震動
            vibrator.vibrate(100)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(count == 4){
                vibrator!!.vibrate(VibrationEffect.createOneShot(90, VibrationEffect.DEFAULT_AMPLITUDE));
            }else{
                vibrator!!.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        } else {
            //deprecated in API 26
            if(count == 4){
                vibrator!!.vibrate(90);
            }else{
                vibrator!!.vibrate(20);
            }
        }
    }
}