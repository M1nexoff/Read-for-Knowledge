package uz.gita.readforknowledge.ui.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.mystorageb8.DownloadData
import uz.gita.readforknowledge.data.model.BookEntity
import uz.gita.readforknowledge.data.model.UIBookData
import uz.gita.readforknowledge.domain.BookRepository
import uz.gita.readforknowledge.domain.impl.BookRepositoryImpl
import java.io.File

class InfoViewModelImpl: InfoViewModel, ViewModel() {
    private val bookRepository:BookRepository = BookRepositoryImpl.getInstance()

    override val progress = MutableLiveData<DownloadData>()
    override val toPdfScreen = MutableLiveData<File>()
    override val file = MutableLiveData<File>()
    override var isInstalled = MutableLiveData<Boolean>()

    override fun openPdf(file: File) {
        toPdfScreen.value = file
    }

    override fun getBook(name: String) {
        file.value = bookRepository.getBookFromLocal(name)
    }

    override fun downLoad(uiBookData: UIBookData) {
        bookRepository.downloadBook(uiBookData.location,uiBookData.id+".pdf")
        bookRepository.downloadBookFileLiveData.observeForever {
            when(it){
                is DownloadData.FinishState ->{
                    bookRepository.addBook(BookEntity(uiBookData.id,uiBookData.name,uiBookData.author,uiBookData.size,0,uiBookData.image))
                }
                else -> {}
            }
            progress.value = it
        }
    }

    override fun pause() {
        bookRepository.pauseDownload()
    }

    override fun resume() {
        bookRepository.resumeDownload()
    }

    override fun delete(s: String) {
        bookRepository.delete(s)
    }

    override fun checkBook(s: String) {
        isInstalled.value = bookRepository.checkBook(s)
    }
}