package com.lhr.notes_kotlin.view.base

import android.app.Application
import android.widget.Toast
import androidx.databinding.ktx.BuildConfig
import androidx.lifecycle.MutableLiveData
import com.lhr.notes_kotlin.sqlLite.SqlDatabase
import com.lhr.notes_kotlin.util.CrashlyticsTree
import timber.log.Timber

class APP: Application() {

    override fun onCreate() {
        super.onCreate()

        SqlDatabase.init(this)
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }else{
            Timber.plant(CrashlyticsTree())
        }

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Timber.e(e)
//            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    companion object{
//        var navAction: NavigationAction? = null
        val nightMode = MutableLiveData(false)
    }

}