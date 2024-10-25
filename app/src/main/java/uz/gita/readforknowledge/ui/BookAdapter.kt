package uz.gita.readforknowledge.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.gita.readforknowledge.R
import uz.gita.readforknowledge.data.model.UIBookData
import uz.gita.readforknowledge.databinding.ItemBookBinding

class BookAdapter(
    val onClick: (UIBookData) -> Unit
) : ListAdapter<UIBookData, BookAdapter.BookVH>(BookDiffUtil) {

    inner class BookVH(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(getItem(adapterPosition))
            }
        }

        fun bind(bookData: UIBookData) {
            binding.name.text = bookData.name
            binding.author.text = bookData.author
            binding.size.text = bookData.size
            Glide.with(binding.imageView.context)
                
                .load(Uri.parse(bookData.image))
                .placeholder(R.drawable.place_holder)
                .into(binding.imageView)
            if (bookData.isInstalled) {
                binding.installed.text = "Installed"
                binding.installed.setBackgroundResource(R.drawable.bg_installed)
            }else{
                binding.installed.text = "Not Installed"
                binding.installed.setBackgroundResource(R.drawable.bg_not_installed)
            }
        }
    }

    object BookDiffUtil : DiffUtil.ItemCallback<UIBookData>() {
        override fun areItemsTheSame(oldItem: UIBookData, newItem: UIBookData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UIBookData, newItem: UIBookData): Boolean =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: BookVH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookVH {
        return BookVH(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

}