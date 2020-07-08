package com.swolfand.ticktock.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Activities(val activities: List<Activity> = emptyList())

@JsonClass(generateAdapter = true)
@Entity(tableName = "activity_table")
data class Activity(
    @PrimaryKey
    val id: Int = -1,
    val order: Int = -1,
    val instructorId: Int = -1,
    val materialId: Int = -1,
    val durationSeconds: Int = -1
)