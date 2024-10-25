package uz.gita.readforknowledge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Book")
data class BookEntity(
    @PrimaryKey val id: String,
    val name: String,
    val author: String,
    val size: String,
    val lastPage: Int,
    val image:String
)