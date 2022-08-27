package ru.netology.bookrecipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeWithStages>>

    @Query(
        """SELECT * FROM recipes,categories
WHERE id == recipe_id AND
category_Id IN (:categoryIds)
GROUP BY id
ORDER BY id ASC
        """
    )
    fun getCategories(categoryIds: Set<Int>): LiveData<List<RecipeWithStages>>

    @Query(
        """SELECT * FROM recipes,categories
WHERE id == recipe_id AND
category_Id IN (:categoryIds) AND
likedByMe == 0
GROUP BY id
ORDER BY id ASC
        """
    )
    fun getCategoriesLiked(categoryIds: List<Int>): LiveData<List<RecipeWithStages>>

    @Insert
    fun insertRecipe(recipe: RecipeEntity): Long

    @Insert
    fun insertStage(stage: StageEntity)

    @Insert
    fun insertCategory(category: CategoryIdEntity)

    @Query(
        """
        UPDATE recipes SET
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun likeById(id: Long)


    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeById(id: Long)


}