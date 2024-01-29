package com.example.notesapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
   suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note ORDER BY dateAdded")
    suspend fun getNotesOrderedByDateAdded():Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY title ASC")
    suspend fun getNotesOrderedByTitle():Flow<List<Note>>
}