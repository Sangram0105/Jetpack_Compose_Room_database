package com.example.notesapp.Presentattion

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val dao:NoteDao
):ViewModel() {

    private val isSortedDateAdded = MutableStateFlow(true)

    private val notes : Flow<List<Note>> =
        isSortedDateAdded.flatMapLatest { sort ->
            if(sort){
                dao.getNotesOrderedByDateAdded()
            }
            else{
                dao.getNotesOrderedByTitle()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state :MutableStateFlow<NoteState> = MutableStateFlow(NoteState())
    val state =
        combine(_state ,  isSortedDateAdded,notes){ state,isSortedDateAdded,notes ->
            state.copy(
                notes=notes
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())




    fun onEvent(event: NotesEvent){

        when(event){
            is NotesEvent.DeleteNotes -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }



            is NotesEvent.SaveNotes -> {
                val note= Note(
                    title = state.value.title.value,
                    description = state.value.description.value,
                    dateAdded = System.currentTimeMillis()
                )

                viewModelScope.launch {
                    dao.upsertNote(note)
                }


             _state.update {
                 it.copy(
                     title = mutableStateOf(""),
                     description = mutableStateOf("")
                 )
             }
            }


            NotesEvent.sortNotes ->{
                isSortedDateAdded.value=! isSortedDateAdded.value
            }
        }
    }
}