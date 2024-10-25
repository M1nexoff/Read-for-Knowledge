package uz.gita.readforknowledge.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.readforknowledge.data.model.UIBookData
import uz.gita.readforknowledge.domain.BookRepository
import uz.gita.readforknowledge.domain.impl.BookRepositoryImpl

class MainViewModelImpl: MainViewModel, ViewModel() {
    private val bookRepository: BookRepository = BookRepositoryImpl.getInstance()
    override val bookList = MutableLiveData<List<UIBookData>>()
    override val toInfoScreen = MutableLiveData<UIBookData>()

    init{
        bookRepository.bookList.observeForever {
            it.onSuccess {
                val list = arrayListOf<UIBookData>()
                val local = bookRepository.getLocalBooks()
                it.forEachIndexed { index, bookData ->
                    if (local.any { it.id == bookData.id}){
                        list.add(UIBookData(bookData.id,bookData.name,bookData.author,bookData.size,true,bookData.location,bookData.image))
                    }else{
                        list.add(UIBookData(bookData.id,bookData.name,bookData.author,bookData.size,false,bookData.location,bookData.image))
                    }
                }
                bookList.value = list
            }
        }
    }

    override fun toInfoScreen(uiBookData: UIBookData) {
        toInfoScreen.value = uiBookData
    }

    override fun update() {
        val list = arrayListOf<UIBookData>()
        val local = bookRepository.getLocalBooks()
        bookList.value?.forEachIndexed { index, bookData ->
            if (local.any { it.id == bookData.id}){
                list.add(UIBookData(bookData.id,bookData.name,bookData.author,bookData.size,true,bookData.location,bookData.image))
            }else{
                list.add(UIBookData(bookData.id,bookData.name,bookData.author,bookData.size,false,bookData.location,bookData.image))
            }
        }
        bookList.value = list
    }

    override fun check(s: String) {
        bookRepository.checkBook(s)
    }


}