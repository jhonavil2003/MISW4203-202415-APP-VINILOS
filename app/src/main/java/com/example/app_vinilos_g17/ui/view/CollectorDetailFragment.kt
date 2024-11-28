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
import com.example.app_vinilos_g17.databinding.FragmentCollectorDetailBinding
import com.example.app_vinilos_g17.viewmodels.CollectorDetailViewModel

class CollectorDetailFragment : Fragment() {

    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!

    private val args: CollectorDetailFragmentArgs by navArgs()
    private lateinit var viewModel: CollectorDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val collectorId = args.collectorId
        viewModel = ViewModelProvider(this, CollectorDetailViewModel.Factory(requireActivity().application, collectorId)).get(
            CollectorDetailViewModel::class.java)

        viewModel.collectorDetail.observe(viewLifecycleOwner) { collector ->
            binding.collectorName.text = collector.name
            binding.collectorTelephone.text = collector.telephone
            binding.collectorEmail.text = collector.email
            (activity as? AppCompatActivity)?.supportActionBar?.title = collector.name
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
        Toast.makeText(activity, "Error de red al cargar el coleccionista", Toast.LENGTH_LONG).show()
        viewModel.onNetworkErrorShown()
    }
}
