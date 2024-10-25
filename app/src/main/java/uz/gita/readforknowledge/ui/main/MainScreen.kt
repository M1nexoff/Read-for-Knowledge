package uz.gita.readforknowledge.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.readforknowledge.R
import uz.gita.readforknowledge.data.model.UIBookData
import uz.gita.readforknowledge.databinding.ScreenMainBinding
import uz.gita.readforknowledge.ui.BookAdapter

class MainScreen : Fragment(R.layout.screen_main) {
    private val binding by viewBinding(ScreenMainBinding::bind)
    private val adapter by lazy {
        BookAdapter {
            viewModel.toInfoScreen(it)
        }
    }
    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
        binding.booksList.adapter = adapter
        viewModel.bookList.observe(viewLifecycleOwner){
            binding.emptyBox.isInvisible = it.isNotEmpty()
            binding.emptyText.isInvisible = it.isNotEmpty()
            adapter.submitList(it)
            it.forEach { viewModel.check(it.id+".pdf") }
        }

        viewModel.toInfoScreen.observe(this,toInfoScreenObserve)
    }
    private val toInfoScreenObserve = Observer<UIBookData>{
        findNavController().navigate(MainScreenDirections.actionMainScreenToInfoScreen(it))
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
    }
}
