package com.swolfand.ticktock.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Instructors(val instructors: List<Instructor>)

@JsonClass(generateAdapter = true)
@Entity(tableName = "instructor_table")
data class Instructor(@PrimaryKey val id: Int = -1, val firstName: String, val lastName: String)