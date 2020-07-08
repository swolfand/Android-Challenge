package com.swolfand.ticktock.seed

import com.swolfand.ticktock.model.Activity
import com.swolfand.ticktock.model.Instructor
import com.swolfand.ticktock.model.Material

val activities = listOf<Activity>(
    Activity(
        id = 110,
        order = 0,
        instructorId = 1,
        materialId = 103,
        durationSeconds = 45
    ),
    Activity(
        id = 111,
        order = 1,
        instructorId = 96,
        materialId = 39,
        durationSeconds = 30
    ),
    Activity(
        id = 112,
        order = 1,
        instructorId = 1,
        materialId = 115,
        durationSeconds = 30
    ),
    Activity(
        id = 113,
        order = 2,
        instructorId = 1,
        materialId = 101,
        durationSeconds = 90
    ),
    Activity(
        id = 114,
        order = 3,
        instructorId = 96,
        materialId = 38,
        durationSeconds = 30
    ),
    Activity(
        id = 115,
        order = 3,
        instructorId = 1,
        materialId = 128,
        durationSeconds = 30
    ),
    Activity(
        id = 116,
        order = 4,
        instructorId = 96,
        materialId = 101,
        durationSeconds = 45
    ),
    Activity(
        id = 117,
        order = 5,
        instructorId = 96,
        materialId = 105,
        durationSeconds = 30
    )
)

val instructors = listOf<Instructor>(
    Instructor(id = 1, givenName = "Jackie", familyName = "Robinson"),
    Instructor(id = 96, givenName = "Babe", familyName = "Ruth")
)

val materials = listOf<Material>(
    Material(id = 128, name = "Robocops"),
    Material(id = 103, name = "Stretch"),
    Material(id = 105, name = "Kicks"),
    Material(id = 115, name = "Flying Changes"),
    Material(id = 39, name = "Follow the Ball"),
    Material(id = 101, name = "Water Time"),
    Material(id = 38, name = "Tag Me")

)