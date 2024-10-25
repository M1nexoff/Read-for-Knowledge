package uz.gita.readforknowledge.ui.pdf

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rajat.pdfviewer.PdfRendererView
import uz.gita.readforknowledge.R
import uz.gita.readforknowledge.databinding.ScreenPdfBinding
import uz.gita.readforknowledge.domain.impl.BookRepositoryImpl

class PdfScreen : Fragment(R.layout.screen_pdf) {
    private val binding by viewBinding(ScreenPdfBinding::bind)
    private val args: PdfScreenArgs by navArgs()
    private val repository = BookRepositoryImpl.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var book = repository.getBook(args.book.id)

        // Initialize PDF with the file
        binding.pdf.initWithFile(args.File)

        // Jump to the la  st saved page of the book
        binding.name.text = book.name
        binding.pdf.jumpToPage(book.lastPage)
        binding.seekBar.max = binding.pdf.totalPageCount - 1
        // Set up StatusListener to update SeekBar and track page changes
        binding.pdf.statusListener = object : PdfRendererView.StatusCallBack {
            override fun onPdfLoadSuccess(absolutePath: String) {
                super.onPdfLoadSuccess(absolutePath)
                // Set SeekBar maximum to total pages (minus 1 because SeekBar index starts at 0)

            }

            override fun onPageChanged(currentPage: Int, totalPage: Int) {
                book = repository.getBook(args.book.id)
                book = book.copy(lastPage = currentPage)
                repository.addBook(book)

                // Update SeekBar progress to match the current page
                binding.seekBar.progress = currentPage
            }
        }

        // Set up SeekBar listener to handle page jumps
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // Jump to the page that corresponds to the SeekBar progress
                    binding.pdf.jumpToPage(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Optional: You can handle logic for when the user starts touching the SeekBar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Optional: You can handle logic for when the user stops touching the SeekBar
            }
        })

        // Handle back button click
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
