package com.swolfand.ticktock.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Activities(val activities: List<Activity> = emptyList())

@JsonClass(generateAdapter = true)
data class Activity(
    val id: Int = -1,
    val order: Int = -1,
    val instructorId: Int = -1,
    val materialId: Int? = -1,
    val durationSeconds: Int? = -1
)