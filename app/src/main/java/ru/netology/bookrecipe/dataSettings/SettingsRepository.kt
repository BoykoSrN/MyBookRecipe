package ru.netology.bookrecipe.dataSettings

interface SettingsRepository {

    fun saveStateSwitch(key: String, b: Boolean)
    fun getStateSwitch(key: String):Boolean

}