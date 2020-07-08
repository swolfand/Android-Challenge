package com.swolfand.ticktock.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swolfand.ticktock.model.Activity
import io.reactivex.Flowable

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activity_table ORDER BY `order` ASC")
    fun getActivities(): Flowable<List<Activity>>

    @Query("DELETE FROM activity_table")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(activity: List<Activity>)
}
