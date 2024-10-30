package com.example.app_vinilos_g17.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.app_vinilos_g17.databinding.FragmentAlbumDetailBinding

class AlbumDetailFragment : Fragment() {
    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val args: AlbumDetailFragmentArgs by navArgs() // Obtiene los argumentos de Safe Args

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)

        // Muestra el id del álbum al que se le hizo clic
        binding.textViewDetail.text = "Le diste click al álbum id: ${args.id}"

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Libera el binding para evitar fugas de memoria
    }
}
