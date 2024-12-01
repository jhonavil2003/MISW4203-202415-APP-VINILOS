package com.example.app_vinilos_g17.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_vinilos_g17.databinding.FragmentAlbumDetailBinding
import com.example.app_vinilos_g17.repositories.AlbumDetailRepository
import com.example.app_vinilos_g17.ui.adapters.CommentAdapter
import com.example.app_vinilos_g17.ui.adapters.PerformerAdapter
import com.example.app_vinilos_g17.ui.adapters.TrackAdapter
import com.example.app_vinilos_g17.viewmodels.AlbumDetailViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject



class AlbumDetailFragment : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailFragmentArgs by navArgs()
    private lateinit var viewModel: AlbumDetailViewModel
    private lateinit var performerAdapter: PerformerAdapter
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var commentAdapter: CommentAdapter
    private val albumRepository = AlbumDetailRepository(requireActivity().application)

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

        binding.addTrack.setOnClickListener {
            if (binding.addtrackForm.visibility == View.GONE){
                binding.addtrackForm.visibility = View.VISIBLE
            } else{
                binding.addtrackForm.visibility = View.GONE
            }
        }

        binding.postButton.setOnClickListener{
            val name = binding.txtPostName.text
            val duration = binding.txtPostDuration.text

            val jsonParams = JSONObject()
            jsonParams.put("name",name)
            jsonParams.put("duration", duration)

            viewModel.viewModelScope.launch{
                try {
                    // Llamamos a la función suspensiva en el repositorio para obtener el detalle del álbum
                    albumRepository.setTrackAlbum(albumId, jsonParams)
                } catch (error: Exception) {
                    // En caso de error, mostramos un mensaje y actualizamos el estado de error
                    Log.d("NetworkError", error.toString())
                }

            }


        }
        binding.recyclerViewPerformers.layoutManager = LinearLayoutManager(context)
        performerAdapter = PerformerAdapter()
        binding.recyclerViewPerformers.adapter = performerAdapter


        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(context)
        trackAdapter = TrackAdapter(emptyList())
        binding.recyclerViewTracks.adapter = trackAdapter


        binding.recyclerViewComments.layoutManager = LinearLayoutManager(context)
        commentAdapter = CommentAdapter(emptyList())
        binding.recyclerViewComments.adapter = commentAdapter

        viewModel.album.observe(viewLifecycleOwner) { album ->
            binding.album = album
            (activity as? AppCompatActivity)?.supportActionBar?.title = album.name


            performerAdapter.updatePerformers(album.performers)


            if (album.tracks.isEmpty()) {
                binding.textViewNoTracks.visibility = View.VISIBLE
                binding.recyclerViewTracks.visibility = View.GONE
            } else {
                binding.textViewNoTracks.visibility = View.GONE
                binding.recyclerViewTracks.visibility = View.VISIBLE
                trackAdapter.updateTracks(album.tracks)
            }


            if (album.comments.isEmpty()) {
                binding.textViewNoComments.visibility = View.VISIBLE
                binding.recyclerViewComments.visibility = View.GONE
            } else {
                binding.textViewNoComments.visibility = View.GONE
                binding.recyclerViewComments.visibility = View.VISIBLE
                commentAdapter.updateComments(album.comments)
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
