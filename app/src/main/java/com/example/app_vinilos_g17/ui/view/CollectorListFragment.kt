package com.example.app_vinilos_g17.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_vinilos_g17.R
import com.example.app_vinilos_g17.databinding.FragmentCollectorListBinding
import com.example.app_vinilos_g17.models.Collector
import com.example.app_vinilos_g17.viewmodels.CollectorsViewModel
import com.example.app_vinilos_g17.ui.adapters.CollectorsAdapter

class CollectorListFragment : Fragment() {
    private var _binding: FragmentCollectorListBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorsViewModel
    private var viewModelAdapter: CollectorsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectorListBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CollectorsAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.fragmentsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_collectors)
        viewModel = ViewModelProvider(this, CollectorsViewModel.Factory(activity.application)).get(CollectorsViewModel::class.java)
        viewModel.collectors.observe(viewLifecycleOwner, Observer<List<Collector>> {
            it.apply {
                viewModelAdapter!!.collectors = this
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
