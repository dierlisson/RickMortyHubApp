package com.dierlisson.rickmortyhub.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dierlisson.rickmortyhub.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM favorites_table")
    suspend fun getAllFavorites(): List<CharacterEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites_table WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(character: CharacterEntity)

    @Delete
    suspend fun deleteFavorite(character: CharacterEntity)
    
    @Query("DELETE FROM favorites_table WHERE id = :id")
    suspend fun deleteFavoriteById(id: Int)
}
