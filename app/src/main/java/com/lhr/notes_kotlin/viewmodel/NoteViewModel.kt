package com.lhr.notes_kotlin.viewmodel

import androidx.lifecycle.LiveData
import com.lhr.notes_kotlin.sqlLite.NotesEntity

interface NoteViewModel {
    val getNotesLiveData: LiveData<List<NotesEntity>>
    val goToAddNoteFragment: LiveData<Unit>

    fun loadAllNotes()

    fun update(noteEntity: NotesEntity)

}