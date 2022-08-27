package ru.netology.bookrecipe.adapter

import ru.netology.bookrecipe.dataModels.Stage

interface StageInteractionListener {

    fun onRemoveStageClicked(stage: Stage)
    fun onSaveStageClicked(content: String, uriPhoto: String?)
    fun onMoveStageUpClicked(position: Int)
    fun onMoveStageDownClicked(position: Int)
}