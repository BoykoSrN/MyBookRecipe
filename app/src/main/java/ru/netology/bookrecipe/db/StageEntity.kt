package ru.netology.bookrecipe.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.netology.bookrecipe.dataModels.Stage

@Entity(
    tableName = "stages",
    // primaryKeys = ["recipe_id", "id"],
    foreignKeys = [ForeignKey(
        entity = RecipeEntity::class,
        parentColumns = ["id"],
        childColumns = ["stage_recipe_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class StageEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stage_id")
    val stageId: Int,

    @ColumnInfo(name = "stage_recipe_id")
    val stage_recipe_Id: Long,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "uri_photo")
    val uriPhoto: String?

) {
    internal fun toModel() = Stage(
        id = stageId,
        recipeId = stage_recipe_Id,
        content = content,
        uriPhoto = uriPhoto,
    )
}