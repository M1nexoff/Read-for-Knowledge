package uz.gita.readforknowledge.data.model

data class FBBookData(
    val name: String,
    val author: String,
    val size: String,
    val location: String,
    val image:String

){
    fun toBook(id: String): BookData = BookData(id,name, author, size, location,image)
}