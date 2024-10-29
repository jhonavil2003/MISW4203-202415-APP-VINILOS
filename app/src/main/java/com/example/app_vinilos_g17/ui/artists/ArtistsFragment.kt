package com.example.app_vinilos_g17.ui.artists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app_vinilos_g17.databinding.FragmentArtistsBinding

class ArtistsFragment : Fragment() {
    private var _binding: FragmentArtistsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val artistsViewModel =
            ViewModelProvider(this).get(ArtistsViewModel::class.java)

        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textArtists
        artistsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}