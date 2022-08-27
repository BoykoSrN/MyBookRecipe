package ru.netology.bookrecipe.data.impl

import androidx.lifecycle.*
import androidx.room.Transaction
import ru.netology.bookrecipe.dataModels.Recipe
import ru.netology.bookrecipe.db.RecipeDao
import ru.netology.bookrecipe.data.RecipeRepository
import ru.netology.bookrecipe.db.CategoryIdEntity


class SQLiteRecipeRepository(
    private val dao: RecipeDao
) : RecipeRepository {

    private var categoriesLiveData: MutableLiveData<Set<Int>> = MutableLiveData()

    override var data: LiveData<List<Recipe>> =
        Transformations.switchMap(categoriesLiveData) { list ->
            dao.getCategories(list).map { entities ->
                entities.map { it.toModel() }
            }
        }

    override fun setFilter(categories: Set<Int>) {
        categoriesLiveData.value = categories
    }

    override fun liked(recipeId: Long) {
        dao.likeById(recipeId)
    }

    override fun delete(recipeId: Long) {
        dao.removeById(recipeId)
    }

    @Transaction
    override fun update(recipe: Recipe) {
        dao.removeById(recipe.id)
        val id = dao.insertRecipe(recipe.toEntity(0).recipeEntity)
        recipe.toEntity(id).stagesEntity.map {
            dao.insertStage(it.copy(stageId = 0))
        }
        recipe.categories.map {
            dao.insertCategory(CategoryIdEntity(0, id, it))
        }
    }

    @Transaction
    override fun insert(recipe: Recipe) {
        val id = dao.insertRecipe(recipe.toEntity(0).recipeEntity)
        recipe.toEntity(id).stagesEntity.map {
            dao.insertStage(it.copy(stageId = 0))
        }
        recipe.categories.map {
            dao.insertCategory(CategoryIdEntity(0, id, it))
        }
    }

}