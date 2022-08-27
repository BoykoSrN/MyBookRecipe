package ru.netology.bookrecipe.dataModels

import kotlinx.serialization.Serializable

@Serializable
data class Stage(
    val id: Int,
    val recipeId: Long,
    val content: String,
    val uriPhoto: String? = null
)