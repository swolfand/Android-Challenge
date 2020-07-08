package com.swolfand.ticktock.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swolfand.ticktock.model.Instructor
import io.reactivex.Flowable

@Dao
interface InstructorDao {

    @Query("SELECT * FROM instructor_table ORDER BY id ASC")
    fun getInstructors(): Flowable<List<Instructor>>

    @Query("SELECT * FROM instructor_table WHERE id = :id")
    fun getInstructor(id: Int): Flowable<Instructor>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(instructors: List<Instructor>)

    @Query("DELETE FROM instructor_table")
    fun clear()
}
