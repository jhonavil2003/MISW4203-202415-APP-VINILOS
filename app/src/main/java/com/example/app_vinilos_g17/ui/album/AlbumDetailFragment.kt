package com.example.app_vinilos_g17.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_vinilos_g17.databinding.FragmentAlbumDetailBinding
import com.example.app_vinilos_g17.ui.adapters.TrackAdapter
import com.example.app_vinilos_g17.ui.adapters.CommentAdapter
import com.example.app_vinilos_g17.viewmodels.AlbumDetailViewModel

class AlbumDetailFragment : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailFragmentArgs by navArgs()
    private lateinit var viewModel: AlbumDetailViewModel
    private lateinit var performerAdapter: PerformerAdapter
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val albumId = args.id
        viewModel = ViewModelProvider(this, AlbumDetailViewModel.Factory(requireActivity().application, albumId)).get(
            AlbumDetailViewModel::class.java)

        // Inicializa el RecyclerView para intérpretes
        binding.recyclerViewPerformers.layoutManager = LinearLayoutManager(context)
        performerAdapter = PerformerAdapter() // Inicializa el Adapter vacío
        binding.recyclerViewPerformers.adapter = performerAdapter

        // Inicializa el RecyclerView para pistas
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(context)
        trackAdapter = TrackAdapter(emptyList()) // Inicializa el Adapter vacío
        binding.recyclerViewTracks.adapter = trackAdapter

        // Inicializa el RecyclerView para comentarios
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(context)
        commentAdapter = CommentAdapter(emptyList()) // Inicializa el Adapter vacío
        binding.recyclerViewComments.adapter = commentAdapter

        viewModel.album.observe(viewLifecycleOwner) { album ->
            binding.album = album
            (activity as? AppCompatActivity)?.supportActionBar?.title = album.name

            // Actualiza la lista de intérpretes
            performerAdapter.updatePerformers(album.performers)

            // Maneja la visibilidad de los tracks
            if (album.tracks.isEmpty()) {
                binding.textViewNoTracks.visibility = View.VISIBLE
                binding.recyclerViewTracks.visibility = View.GONE
            } else {
                binding.textViewNoTracks.visibility = View.GONE
                binding.recyclerViewTracks.visibility = View.VISIBLE
                trackAdapter.updateTracks(album.tracks) // Actualiza el Adapter con las pistas
            }

            // Maneja la visibilidad de los comentarios
            if (album.comments.isEmpty()) {
                binding.textViewNoComments.visibility = View.VISIBLE
                binding.recyclerViewComments.visibility = View.GONE
            } else {
                binding.textViewNoComments.visibility = View.GONE
                binding.recyclerViewComments.visibility = View.VISIBLE
                commentAdapter.updateComments(album.comments) // Actualiza el Adapter con los comentarios
            }
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        Toast.makeText(activity, "Error de red al cargar el álbum", Toast.LENGTH_LONG).show()
        viewModel.onNetworkErrorShown()
    }
}
