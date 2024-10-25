package uz.gita.readforknowledge.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.StorageTask
import uz.gita.mystorageb8.DownloadData
import uz.gita.readforknowledge.data.model.BookData
import uz.gita.readforknowledge.data.model.BookEntity
import java.io.File

interface BookRepository {var task: StorageTask<FileDownloadTask.TaskSnapshot>
    val bookList: LiveData<Result<List<BookData>>>
    val downloadBookFileLiveData: LiveData<DownloadData>
    fun downloadBook(path: String, name: String)
    fun pauseDownload()
    fun cancelDownload()
    fun resumeDownload()
    fun getLocalBooks(): List<BookEntity>
    fun addBook(bookEntity: BookEntity)
    fun getBookFromLocal(name: String): File
    abstract fun checkBook(s: String): Boolean
    fun delete(s: String)
    abstract fun getBook(id: String): BookEntity
}