package com.example.app_vinilos_g17.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.app_vinilos_g17.databinding.FragmentArtistDetailBinding
import com.example.app_vinilos_g17.viewmodels.ArtistDetailViewModel

class ArtistDetailFragment : Fragment() {

    private var _binding: FragmentArtistDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ArtistDetailFragmentArgs by navArgs()
    private lateinit var viewModel: ArtistDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val artistId = args.id
        viewModel = ViewModelProvider(this, ArtistDetailViewModel.Factory(requireActivity().application, artistId))[ArtistDetailViewModel::class.java]

        viewModel.artist.observe(viewLifecycleOwner) { artist ->
            binding.artist = artist
            (activity as? AppCompatActivity)?.supportActionBar?.title = artist.name
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