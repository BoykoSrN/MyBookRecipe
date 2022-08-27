package ru.netology.bookrecipe.data

import androidx.lifecycle.LiveData
import ru.netology.bookrecipe.dataModels.Recipe

interface RecipeRepository {

    val data:LiveData<List<Recipe>>

    fun insert(recipe: Recipe)
    fun update(recipe: Recipe)
    fun delete(recipeId:Long)
    fun liked(recipeId:Long)
    fun setFilter(categories: Set<Int>)
}