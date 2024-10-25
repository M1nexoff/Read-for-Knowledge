package uz.gita.readforknowledge.ui.info

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uz.gita.mystorageb8.DownloadData
import uz.gita.readforknowledge.R
import uz.gita.readforknowledge.databinding.ScreenInfoBinding
import java.io.File

class InfoScreen: Fragment(R.layout.screen_info) {
    private val binding by viewBinding(ScreenInfoBinding::bind)
    private val viewModel: InfoViewModel by viewModels<InfoViewModelImpl>()
    private val navArgs: InfoScreenArgs by navArgs()
    private var file: File? = null
    var isDownloaded = true
    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        if (navArgs.UIBookData.isInstalled){
            viewModel.checkBook(navArgs.UIBookData.id+".pdf")
        }
        binding.name.text = navArgs.UIBookData.name
        binding.author.text = navArgs.UIBookData.author
        binding.size.text = navArgs.UIBookData.size
        Glide.with(binding.root.context)
            .load(navArgs.UIBookData.image)
            .apply(RequestOptions().placeholder(R.drawable.place_holder))
            .into(binding.imageView)
        viewModel.isInstalled.observe(viewLifecycleOwner){
            if (it){
                viewModel.getBook(navArgs.UIBookData.id+".pdf")
            }
        }
        viewModel.progress.observe(viewLifecycleOwner) {
            when(it){
                DownloadData.CancelState -> binding.progressView.visibility = View.INVISIBLE
                is DownloadData.Error -> binding.progressView.visibility = View.INVISIBLE
                is DownloadData.FinishState -> {
                    binding.download.visibility = View.INVISIBLE
                    binding.progressView.progress = 100F
                    binding.open.isInvisible = false
                    binding.delete.isInvisible = false
                    isDownloaded = true
                    binding.pause.isInvisible = true
                    file = it.file
                    binding.progressView.labelText = "Ready"
                }
                DownloadData.PauseState -> {
                    binding.open.isInvisible = true
                    binding.delete.isInvisible = true
                    isDownloaded = false
                    binding.progressView.labelText = "Pause"
                    binding.pause.setOnClickListener {
                        viewModel.resume()
                        binding.pause.setImageResource(R.drawable.ic_pause)
                    }
                }
                is DownloadData.ProgressData -> {
                    binding.pause.visibility = View.VISIBLE
                    binding.open.isInvisible = true
                    binding.delete.isInvisible = true
                    isDownloaded = false
                    binding.pause.setOnClickListener {
                        viewModel.pause()
                        binding.pause.setImageResource(R.drawable.ic_downloading)
                    }
                    binding.progressView.visibility = View.VISIBLE
                    binding.progressView.progress = it.percent.toFloat()
                    binding.progressView.labelText = "Downloading " + it.percent.toString() + "%"
                }
            }
        }
        binding.appBarLayout
        binding.download.setOnClickListener {
            viewModel.downLoad(navArgs.UIBookData)
            binding.open.isInvisible = true
            binding.delete.isInvisible = true
            isDownloaded = false
            binding.pause.isInvisible = false
            binding.download.isInvisible = true
        }
        binding.open.setOnClickListener {
            viewModel.openPdf(file!!)
        }
        viewModel.toPdfScreen.observe(this,toPdfScreen)
        viewModel.file.observe(viewLifecycleOwner){
            binding.download.visibility = View.INVISIBLE
            binding.progressView.progress = 100F
            file = it
        }
        requireActivity().onBackPressedDispatcher.addCallback {
            if (!isDownloaded){
                AlertDialog.Builder(requireContext()).setTitle("Delete?")
                    .setMessage("Want to delete downloaded?")
                    .setNegativeButton(
                        "No"
                    ) { dialog, which ->
                        dialog?.dismiss()
                    }
                    .setPositiveButton("Yes") { dialog, which ->
                        viewModel.delete(navArgs.UIBookData.id + ".pdf")
                        dialog.dismiss()
                        findNavController().popBackStack()
                    }.create().show()
            }else{
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
    private val toPdfScreen = Observer<File>{
        findNavController().navigate(InfoScreenDirections.actionInfoScreenToPdfScreen(it,navArgs.UIBookData))
    }
}