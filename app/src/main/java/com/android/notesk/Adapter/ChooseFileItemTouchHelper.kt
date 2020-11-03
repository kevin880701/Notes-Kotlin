package com.android.notesk.Adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG
import androidx.recyclerview.widget.RecyclerView
import com.android.notesk.SQLite.NotesEntity
import com.android.notesk.util.ChooseFile.ChooseFilePresenter
import java.util.*


class ChooseFileItemTouchHelper(recyclerView: RecyclerView,arrayList: ArrayList<NotesEntity>,adapter: ChooseFileAdapter,presenter: ChooseFilePresenter) {
    var starPosition = 0
    var endPosition = 0
    var clickState = 0
    val helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder): Int {
            return makeMovementFlags(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
            )
        }

        override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
            endPosition = target.adapterPosition
            val position_dragged = viewHolder.adapterPosition
            val position_target = target.adapterPosition
            Collections.swap(arrayList, position_dragged, position_target)
            adapter.notifyItemMoved(position_dragged, position_target)
            return true //管理上下拖曳
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //管理滑動情形
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            //actionState:點擊=2(ACTION_STATE_DRAG) 放開=0(ACTION_STATE_IDLE)
            clickState = actionState
            if (actionState == ACTION_STATE_DRAG) {
                starPosition = (viewHolder as RecyclerView.ViewHolder).adapterPosition
            } else {
                presenter.changeRecyclerPosition(starPosition, endPosition)
            }
        }
    }).attachToRecyclerView(recyclerView)
}