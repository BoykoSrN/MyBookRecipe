package ru.netology.bookrecipe.db

import androidx.room.Embedded
import androidx.room.Relation
import ru.netology.bookrecipe.dataModels.Recipe

data class RecipeWithStages(

    @Embedded
    val recipeEntity: RecipeEntity,

    @Relation(parentColumn = "id", entityColumn = "recipe_id")
    val categoriesEntity: List<CategoryIdEntity>,

    @Relation(parentColumn = "id", entityColumn = "stage_recipe_id")
    val stagesEntity: List<StageEntity>

) {

    internal fun toModel() = Recipe(
        id = recipeEntity.id,
        title = recipeEntity.title,
        author = recipeEntity.author,
        liked = recipeEntity.liked,
        categories = categoriesEntity.map {
            it.categoryId
        },
        stages = stagesEntity.map { stageEntity ->
            stageEntity.toModel()
        }
    )
}