package com.example.app_vinilos_g17.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app_vinilos_g17.databinding.FragmentArtistListBinding
import com.example.app_vinilos_g17.viewmodels.ArtistsViewModel

class ArtistListFragment : Fragment() {
    private var _binding: FragmentArtistListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val artistsViewModel =
            ViewModelProvider(this).get(ArtistsViewModel::class.java)

        _binding = FragmentArtistListBinding.inflate(inflater, container, false)
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