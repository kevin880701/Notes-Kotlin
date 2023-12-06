package com.lhr.notes_kotlin.viewmodel.implementations

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lhr.notes.data.NotesRepository
import com.lhr.notes_kotlin.sqlLite.NotesEntity
import com.lhr.notes_kotlin.sqlLite.SqlDatabase
import com.lhr.notes_kotlin.viewmodel.NoteViewModel
import timber.log.Timber

class NoteViewModelImpl: NoteViewModel, ViewModel() {
    private val repository = NotesRepository.getInstance(SqlDatabase.getInstance().getNotesDao())

    override val getNotesLiveData = MutableLiveData<List<NotesEntity>>()
    override val goToAddNoteFragment = MutableLiveData<Unit>()

    init {
        loadAllNotes()
    }
    override fun loadAllNotes() {
        getNotesLiveData.postValue(repository.loadNotes())
    }

    override fun update(noteEntity: NotesEntity) {
        repository.updateNotes(noteEntity)
        getNotesLiveData.postValue(repository.loadNotes())
    }


}