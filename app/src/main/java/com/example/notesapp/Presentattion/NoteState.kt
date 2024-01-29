package com.example.notesapp.Presentattion

import android.icu.text.CaseMap.Title
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.notesapp.data.Note

data class NoteState (
    val notes:List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf("")
)

