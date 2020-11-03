package com.android.notesk.ScrollView

import android.view.View
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.android.notesk.ScrollView.ObservableScrollView.ScrollViewListener
import com.android.notesk.SeekBar.VerticalSeekBar


class ScrollBindHelper(
    private val seekBar: VerticalSeekBar,
    private val scrollView: ScrollView
) :
    OnSeekBarChangeListener, ScrollViewListener {
    private val scrollContent: View
    private var isUserSeeking = false
    private val contentRange: Int
        private get() {
            seekBar.max = scrollContent.getHeight() - scrollView.height
            return scrollView.scrollY
        }
    private val scrollRange: Int
        private get() {
            System.out.println(scrollContent.getHeight() - scrollView.height)
            return scrollContent.getHeight() - scrollView.height
        }

    fun bind(seekBar: VerticalSeekBar, scrollView: ObservableScrollView): ScrollBindHelper {
        val helper = ScrollBindHelper(seekBar, scrollView)
        seekBar.setOnSeekBarChangeListener(helper)
        scrollView.setListener(helper)
        return helper
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (!fromUser) {
            //將拖動的換百分比算成Y值，並對映到SrollView上。
            scrollView.scrollTo(0, progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        isUserSeeking = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        isUserSeeking = false
    }

    override fun onScrollChanged(
        scrollView: ObservableScrollView?,
        x: Int,
        y: Int,
        oldx: Int,
        oldy: Int
    ) {
        showScroll()
        if (isUserSeeking) {
            return
        }
        //計算當前滑動位置相對於整個範圍的百分比，並對映到SeekBar上
        val range = contentRange
        seekBar.progress = if (range != 0) range else 0
    }

    private fun showScroll() {
        seekBar.visibility = View.VISIBLE
    }

    /**
     * 使用靜態方法來繫結邏輯，程式碼可讀性更高。
     */
    init {
        scrollContent = scrollView.getChildAt(0)
    }
}