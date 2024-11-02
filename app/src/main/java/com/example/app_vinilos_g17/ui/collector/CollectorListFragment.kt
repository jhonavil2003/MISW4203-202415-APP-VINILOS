package com.example.app_vinilos_g17.ui.collector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app_vinilos_g17.databinding.FragmentCollectorListBinding
import com.example.app_vinilos_g17.viewmodels.CollectorsViewModel


class CollectorListFragment : Fragment() {
    private var _binding: FragmentCollectorListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val collectorsViewModel =
            ViewModelProvider(this).get(CollectorsViewModel::class.java)

        _binding = FragmentCollectorListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCollectors
        collectorsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}