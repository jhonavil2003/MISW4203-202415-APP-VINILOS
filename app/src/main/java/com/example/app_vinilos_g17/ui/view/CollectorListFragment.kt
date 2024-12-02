package com.example.app_vinilos_g17.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_vinilos_g17.R
import com.example.app_vinilos_g17.databinding.FragmentCollectorListBinding
import com.example.app_vinilos_g17.ui.adapters.CollectorsAdapter
import com.example.app_vinilos_g17.viewmodels.CollectorsViewModel

class CollectorListFragment : Fragment() {
    private var _binding: FragmentCollectorListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorsViewModel
    private lateinit var viewModelAdapter: CollectorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.collectorRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModelAdapter = CollectorsAdapter()
        recyclerView.adapter = viewModelAdapter

        viewModel = ViewModelProvider(this, CollectorsViewModel.Factory(activity!!.application)).get(CollectorsViewModel::class.java)

        // Observa la lista de coleccionistas y actualiza el adaptador
        viewModel.collectors.observe(viewLifecycleOwner) { collectors ->
            viewModelAdapter.collectors = collectors
        }

        // Observa el estado de error de red
        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }

        activity?.actionBar?.title = getString(R.string.title_collectors)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (viewModel.isNetworkErrorShown.value != true) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
