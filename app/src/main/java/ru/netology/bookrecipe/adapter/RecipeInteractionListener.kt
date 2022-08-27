package ru.netology.bookrecipe.adapter

import ru.netology.bookrecipe.dataModels.Recipe

interface RecipeInteractionListener {

    fun onRemoveClicked(recipeId: Long)
    fun onEditClicked(recipe: Recipe)
    fun onToRecipe(recipe: Recipe)
    fun onLikeClicked(recipeId: Long)
}