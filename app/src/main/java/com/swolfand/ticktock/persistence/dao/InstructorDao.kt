package com.swolfand.ticktock.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import com.swolfand.ticktock.model.Instructor
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface InstructorDao {

    @Query("SELECT * FROM instructor_table ORDER BY id ASC")
    fun getInstructors(): Flowable<List<Instructor>>

    @Query("SELECT * FROM instructor_table WHERE id = :id")
    fun getInstructor(id: Int): Single<Instructor>

    @Query("DELETE FROM instructor_table")
    fun clear()
}
