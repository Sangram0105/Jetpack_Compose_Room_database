package com.example.notesapp.Presentattion

import android.accounts.AuthenticatorDescription
import com.example.notesapp.data.Note

sealed interface NotesEvent{
     object sortNotes: NotesEvent
    data class DeleteNotes(val note: Note) : NotesEvent
    data class SaveNotes(
        val title:String,
        val description: String
    ) : NotesEvent

}