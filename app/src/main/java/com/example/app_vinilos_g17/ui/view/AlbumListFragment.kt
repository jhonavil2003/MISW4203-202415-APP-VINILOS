package com.example.app_vinilos_g17.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
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
    ): View {
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

        binding.fabAddAlbum.setOnClickListener {
            showAddAlbumDialog()
        }

        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            albums?.let {
                viewModelAdapter.albums = it // Actualiza el adaptador con los nuevos datos
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.albumsRv.visibility =
                if (isLoading) View.GONE else View.VISIBLE // Oculta el RecyclerView mientras carga
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddAlbumDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_album, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Nuevo Álbum")
            .setPositiveButton("Crear") { dialog, _ ->
                val albumName = dialogView.findViewById<EditText>(R.id.editTextAlbumName).text.toString()
                val albumCover = dialogView.findViewById<EditText>(R.id.editTextAlbumCover).text.toString()
                val albumReleaseDate = dialogView.findViewById<EditText>(R.id.editTextAlbumReleaseDate).text.toString()
                val albumDescription = dialogView.findViewById<EditText>(R.id.editTextAlbumDescription).text.toString()
                val albumGenre = dialogView.findViewById<Spinner>(R.id.spinnerAlbumGenre).selectedItem.toString()
                val albumRecordLabel = dialogView.findViewById<Spinner>(R.id.spinnerAlbumRecordLabel).selectedItem.toString()

                // Handle adding the new album here
                val newAlbum = mapOf(
                    "name" to albumName,
                    "cover" to albumCover,
                    "releaseDate" to albumReleaseDate,
                    "description" to albumDescription,
                    "genre" to albumGenre,
                    "recordLabel" to albumRecordLabel
                )
                // Call your ViewModel or repository to add the new album
                viewModel.createAlbum(newAlbum)

                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        dialogBuilder.create().show()
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}