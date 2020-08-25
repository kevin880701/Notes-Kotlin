package com.android.notesk.Model

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.notesk.R
import com.android.notesk.util.Presenter.ChooseFilePresenter
import com.android.notesk.SQLite.NotesEntity
import com.android.notesk.util.View.ChooseFileActivity.Companion.allList
import java.io.File


class MyAdapter( presenter : ChooseFilePresenter) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    var presenter = presenter;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_content, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fileTitle.text = allList.get(position).title

        if(File(allList.get(position).picPath).exists()){
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(allList.get(position).picPath))
        }else{
            allList.get(position).picPath = ""
            var notesEntity = NotesEntity()
            notesEntity.id = allList.get(position).id
            notesEntity.title = allList.get(position).title
            notesEntity.content = allList.get(position).content
            notesEntity.picPath = ""
            presenter.picGone(notesEntity)
        }
        holder.itemView.setOnClickListener {
            presenter?.editFilePage(allList.get(position))
        }
    }

    override fun getItemCount(): Int {
        return allList.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val fileTitle: TextView = v.findViewById(R.id.fileTitle)
        val imageView: ImageView = v.findViewById(R.id.imageView)
    }

    fun update(modelList:ArrayList<NotesEntity>){
        allList = modelList
        this!!.notifyDataSetChanged()
    }
}