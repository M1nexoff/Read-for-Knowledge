package uz.gita.mystorageb8

import java.io.File

sealed interface DownloadData {
    data class FinishState(val file: File) : DownloadData
    data object PauseState : DownloadData
    data object CancelState : DownloadData
    data class ProgressData(val percent : Int) : DownloadData
    data class Error(val message: String) : DownloadData
}


//enum class X(val file : File) {
//    FINISH_STATE, RESUME_STATE
//}
