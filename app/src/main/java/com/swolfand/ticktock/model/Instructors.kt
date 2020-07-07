package com.swolfand.ticktock.model

import androidx.room.Entity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Instructors(val instructors: List<Instructor>)

@JsonClass(generateAdapter = true)
@Entity(tableName = "instructor_table")
data class Instructor(val id: Int = -1, val givenName: String, val familyName: String)