package com.swolfand.ticktock.model

import androidx.room.Entity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Materials(val materials: List<Material>)

@JsonClass(generateAdapter = true)
@Entity(tableName = "material_table")
data class Material(val id: Int = -1, val name: String)