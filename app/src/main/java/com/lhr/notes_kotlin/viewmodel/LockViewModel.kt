package com.lhr.notes_kotlin.viewmodel

import android.os.Vibrator
import com.lhr.notes_kotlin.view.activity.LockActivity

interface LockViewModel {
    var count: Int
    var realCount: Int
    fun real(lockActivity: LockActivity)
    fun count(lockActivity: LockActivity, vibrator: Vibrator)
    fun vibrator(vibrator: Vibrator)

}