package com.swolfand.ticktock.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Materials(val materials: List<Material>)

@JsonClass(generateAdapter = true)
data class Material(val id: Int = -1, val name: String)