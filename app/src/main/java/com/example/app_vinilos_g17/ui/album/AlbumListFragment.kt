package com.example.app_vinilos_g17.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_vinilos_g17.ui.adapters.AlbumListAdapter
import com.example.app_vinilos_g17.databinding.FragmentAlbumListBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.app_vinilos_g17.R
import com.example.app_vinilos_g17.viewmodels.AlbumListViewModel


class AlbumListFragment : Fragment() {

    private var _binding: FragmentAlbumListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumListViewModel by viewModels { AlbumListViewModel.Factory(requireActivity().application) }
    private lateinit var viewModelAdapter: AlbumListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModelAdapter = AlbumListAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.title_albums)

        binding.albumsRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        viewModel.albums.observe(viewLifecycleOwner, Observer { albums ->
            albums?.let {
                viewModelAdapter.albums = it // Actualiza el adaptador con los nuevos datos
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.albumsRv.visibility = if (isLoading) View.GONE else View.VISIBLE // Oculta el RecyclerView mientras carga
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}