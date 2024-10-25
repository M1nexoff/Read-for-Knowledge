package uz.gita.readforknowledge.ui.info

import androidx.lifecycle.LiveData
import uz.gita.mystorageb8.DownloadData
import uz.gita.readforknowledge.data.model.UIBookData
import java.io.File

interface InfoViewModel {
    val progress: LiveData<DownloadData>
    val toPdfScreen: LiveData<File>
    val file: LiveData<File>

    fun openPdf(file: File)
    fun getBook(name: String)
    fun downLoad(uiBookData: UIBookData)
    fun pause()
    fun resume()
    fun delete(s: String)
    fun checkBook(s: String)
    val isInstalled: LiveData<Boolean>
}