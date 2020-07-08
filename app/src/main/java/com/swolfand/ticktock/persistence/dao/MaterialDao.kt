package com.swolfand.ticktock.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swolfand.ticktock.model.Material
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MaterialDao {

    @Query("SELECT * FROM material_table ORDER BY id ASC")
    fun getMaterials(): Flowable<List<Material>>

    @Query("SELECT * FROM material_table WHERE id = :id")
    fun getMaterial(id: Int): Flowable<Material>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(materials: List<Material>)

    @Query("DELETE FROM material_table")
    fun clear()
}
