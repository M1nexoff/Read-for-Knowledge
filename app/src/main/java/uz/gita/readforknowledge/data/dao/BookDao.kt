package uz.gita.readforknowledge.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.gita.readforknowledge.data.model.BookEntity

@Dao
interface BookDao {
    @Query("select * from Book")
    fun getAll(): List<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(bookEntity: BookEntity)

    @Delete
    fun delete(bookEntity: BookEntity)
    @Query("select * from Book where id == :id limit 1")
    abstract fun get(id: String): BookEntity
}