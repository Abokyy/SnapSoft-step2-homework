package com.snapsoft.homework.model.DTOs

import java.io.Serializable

data class MovieDTO(
    val id: Int,
    val title: String,
    val poster_path: String,
    val budget: Int,
    val vote_average: Double
): Serializable