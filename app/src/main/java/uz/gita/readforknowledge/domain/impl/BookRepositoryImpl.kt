package uz.gita.readforknowledge.domain.impl

import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.storage
import uz.gita.mystorageb8.DownloadData
import uz.gita.readforknowledge.app.App
import uz.gita.readforknowledge.data.database.AppDataBase
import uz.gita.readforknowledge.data.model.BookData
import uz.gita.readforknowledge.data.model.BookEntity
import uz.gita.readforknowledge.domain.BookRepository
import java.io.File
import java.io.IOException

private fun DocumentSnapshot.toBook(id: String): BookData = BookData(
    id,
    data!![BookData::name.name].toString(),
    data!![BookData::author.name].toString(),
    data!![BookData::size.name].toString(),
    data!![BookData::location.name].toString(),
    data!![BookData::image.name].toString(),
)

class BookRepositoryImpl: BookRepository {
    companion object {
        private lateinit var itemInstance: BookRepositoryImpl
        fun getInstance(): BookRepositoryImpl {
            if (!Companion::itemInstance.isInitialized) {
                itemInstance = BookRepositoryImpl()
            }
            return itemInstance
        }
    }

    private val firestore = Firebase.firestore
    private val storage = Firebase.storage.reference
    private val bookDao = AppDataBase.getInstance().getBookDao()

    override lateinit var task : StorageTask<FileDownloadTask.TaskSnapshot>
    override val bookList = MutableLiveData<Result<List<BookData>>>()
    override val downloadBookFileLiveData: MutableLiveData<DownloadData> = MutableLiveData()

    init {
        firestore.collection(BookData::class.simpleName!!)
            .addSnapshotListener { value, error ->
                if (error == null) {
                    bookList.value = Result.success(value!!.map { it.toBook(it.id) })
                }else{
                    bookList.value = Result.failure(error)
                }
            }
    }


    override fun downloadBook(path: String, name: String) {
        val directoryPath: String
        val directory: File

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use scoped storage
            directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "bookapp")
            directoryPath = directory.absolutePath
        } else {
            // Use legacy external storage
            directoryPath = Environment.getExternalStorageDirectory().toString() + "/bookapp"
            directory = File(directoryPath)
        }

        if (!directory.exists()) {
            val directoryCreated = directory.mkdirs()
            Log.d("Directory Creation", "Directory created: $directoryCreated at $directoryPath")
            if (!directoryCreated) {
                Log.e("Directory Creation", "Failed to create directory: $directoryPath")
                return
            }
        } else {
            Log.d("Directory Creation", "Directory already exists: $directoryPath")
        }

        val file = File(directory, name)
        if(file.exists()){
            Log.d("QQQ",file.delete().toString())
        }
        try {
            val fileCreated = file.createNewFile()
            Log.d("File Creation", "File created: $fileCreated at ${file.absolutePath}")
        } catch (e: IOException) {
            Log.e("File Creation", "Failed to create file at ${file.absolutePath}", e)
            return
        }
        task = storage.child(path)
            .getFile(file)
            .addOnSuccessListener { downloadBookFileLiveData.value = DownloadData.FinishState(file) }
            .addOnProgressListener { downloadBookFileLiveData.value = DownloadData.ProgressData((it.bytesTransferred * 100 / it.totalByteCount).toInt()) }
            .addOnPausedListener { downloadBookFileLiveData.value = DownloadData.PauseState }
            .addOnCanceledListener { downloadBookFileLiveData.value = DownloadData.CancelState }
            .addOnFailureListener { downloadBookFileLiveData.value = DownloadData.Error(it.message ?: "Unknown message") }
    }
    override fun pauseDownload() {
        if (::task.isInitialized)
            task.pause()
    }

    override fun cancelDownload() {
        if (::task.isInitialized)
            task.cancel()
    }

    override fun resumeDownload() {
        val books = listOf(
            Book(
                name = "Pride and Prejudice",
                author = "Jane Austen",
                size = "1.2 MB",
                image = "https://www.gutenberg.org/cache/epub/1342/pg1342.cover.medium.jpg"
            ),
            Book(
                name = "Moby Dick",
                author = "Herman Melville",
                size = "1.5 MB",
                image = "https://www.gutenberg.org/cache/epub/2701/pg2701.cover.medium.jpg"
            ),
            Book(
                name = "The Adventures of Sherlock Holmes",
                author = "Arthur Conan Doyle",
                size = "1.0 MB",
                image = "https://www.gutenberg.org/cache/epub/244/pg244.cover.medium.jpg"
            ),
            Book(
                name = "Frankenstein",
                author = "Mary Shelley",
                size = "1.1 MB",
                image = "https://www.gutenberg.org/cache/epub/84/pg84.cover.medium.jpg"
            ),
            Book(
                name = "The Picture of Dorian Gray",
                author = "Oscar Wilde",
                size = "0.9 MB",
                image = "https://www.gutenberg.org/cache/epub/174/pg174.cover.medium.jpg"
            ),
            Book(
                name = "The Count of Monte Cristo",
                author = "Alexandre Dumas",
                size = "1.7 MB",
                image = "https://www.gutenberg.org/cache/epub/1184/pg1184.cover.medium.jpg"
            ),
            Book(
                name = "War and Peace",
                author = "Leo Tolstoy",
                size = "2.0 MB",
                image = "https://www.gutenberg.org/cache/epub/2600/pg2600.cover.medium.jpg"
            ),
            Book(
                name = "The Great Gatsby",
                author = "F. Scott Fitzgerald",
                size = "0.8 MB",
                image = "https://www.gutenberg.org/cache/epub/64317/pg64317.cover.medium.jpg"
            ),
            Book(
                name = "Jane Eyre",
                author = "Charlotte Brontë",
                size = "1.3 MB",
                image = "https://www.gutenberg.org/cache/epub/1260/pg1260.cover.medium.jpg"
            ),
            Book(
                name = "A Tale of Two Cities",
                author = "Charles Dickens",
                size = "1.4 MB",
                image = "https://www.gutenberg.org/cache/epub/98/pg98.cover.medium.jpg"
            )
        )
        books.forEach {
//            firestore.collection(BookData::class.simpleName.toString()).add(it)
        }
        if (::task.isInitialized)
            task.resume()
    }

    override fun getLocalBooks(): List<BookEntity>{
        return bookDao.getAll()
    }

    override fun addBook(bookEntity: BookEntity){
        bookDao.add(bookEntity)
    }
    override fun delete(s:String){
        val directory = createDirectory()
        val file = File(directory, s)
        if (file.exists()) {
            file.delete()
        }
        task.cancel()
        bookDao.delete(BookEntity(s.substring(0,s.length-4),"","","",0,""))
    }

    override fun getBook(id: String): BookEntity {
        return bookDao.get(id)
    }

    override fun getBookFromLocal(name: String): File {
        val directory = createDirectory()

        val file = File(directory, name)
        if (!file.exists()) {
            try {
                val fileCreated = file.createNewFile()
                Log.d("File Creation", "File created: $fileCreated at ${file.absolutePath}")
            } catch (e: IOException) {
                Log.e("File Creation", "Failed to create file at ${file.absolutePath}", e)
                e.printStackTrace()
            }
        }

        return file
    }

    private fun createDirectory(): File {
        val directoryPath: String
        val directory: File

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use scoped storage for Android 10 and above
            directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "bookapp")
            directoryPath = directory.absolutePath
        } else {
            // Use legacy external storage for below Android 10
            directoryPath = Environment.getExternalStorageDirectory().toString() + "/bookapp"
            directory = File(directoryPath)
        }

        if (!directory.exists()) {
            val directoryCreated = directory.mkdirs()
            Log.d("Directory Creation", "Directory created: $directoryCreated at $directoryPath")
            if (!directoryCreated) {
                Log.e("Directory Creation", "Failed to create directory: $directoryPath")
                throw IOException("Failed to create directory: $directoryPath")
            }
        } else {
            Log.d("Directory Creation", "Directory already exists: $directoryPath")
        }
        return directory
    }

    override fun checkBook(s: String): Boolean {
        val directory = createDirectory()
        val file = File(directory, s)
        if (!file.exists()) {
            try {
                bookDao.delete(BookEntity(s.substring(0,s.length-4),"","","",0,""))
                return false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return true
    }
}