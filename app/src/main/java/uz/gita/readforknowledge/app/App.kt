package uz.gita.readforknowledge.app

import android.app.Application
import android.content.Context
import uz.gita.readforknowledge.data.database.AppDataBase

class App: Application() {
    companion object{
        var contex: Context? = null
    }
    override fun onCreate() {
        super.onCreate()
        contex = this.applicationContext
        AppDataBase.init(this)
    }
}