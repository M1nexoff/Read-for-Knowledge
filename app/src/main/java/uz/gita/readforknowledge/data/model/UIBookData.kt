package uz.gita.readforknowledge.data.model

import java.io.Serializable

data class UIBookData(
    val id: String,
    val name: String,
    val author: String,
    val size: String,
    val isInstalled: Boolean,
    val location: String,
    val image:String

):Serializable