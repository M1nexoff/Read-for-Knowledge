package uz.gita.readforknowledge.ui.main

import androidx.lifecycle.LiveData
import uz.gita.readforknowledge.data.model.UIBookData

interface MainViewModel {
    val bookList: LiveData<List<UIBookData>>
    val toInfoScreen: LiveData<UIBookData>

    fun toInfoScreen(uiBookData: UIBookData)
    fun update()
    abstract fun check(s: String)
}