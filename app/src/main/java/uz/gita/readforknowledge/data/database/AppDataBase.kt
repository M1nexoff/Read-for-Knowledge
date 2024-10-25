package uz.gita.readforknowledge.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.readforknowledge.data.dao.BookDao
import uz.gita.readforknowledge.data.model.BookEntity

@Database(entities = [BookEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    companion object{
        private var instance: AppDataBase? = null
        fun init(context: Context){
            instance = Room.databaseBuilder(context.applicationContext,AppDataBase::class.java,"Book.db").allowMainThreadQueries().build()
        }
        fun getInstance(): AppDataBase{
            return instance!!
        }
    }

    abstract fun getBookDao(): BookDao
}