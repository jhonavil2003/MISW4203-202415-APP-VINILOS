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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_vinilos_g17.databinding.FragmentCollectorDetailBinding
import com.example.app_vinilos_g17.ui.adapters.CommentAdapter
import com.example.app_vinilos_g17.ui.adapters.PerformerAdapter
import com.example.app_vinilos_g17.viewmodels.CollectorDetailViewModel

class CollectorDetailFragment : Fragment() {

    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!

    private val args: CollectorDetailFragmentArgs by navArgs()
    private lateinit var viewModel: CollectorDetailViewModel
    private lateinit var performerAdapter: PerformerAdapter
    private lateinit var commentAdapter: CommentAdapter

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
        viewModel = ViewModelProvider(
            this,
            CollectorDetailViewModel.Factory(requireActivity().application, collectorId)
        ).get(CollectorDetailViewModel::class.java)

        binding.recyclerViewPerformers.layoutManager = LinearLayoutManager(context)
        performerAdapter = PerformerAdapter()
        binding.recyclerViewPerformers.adapter = performerAdapter


        binding.recyclerViewComments.layoutManager = LinearLayoutManager(context)
        commentAdapter = CommentAdapter(emptyList())
        binding.recyclerViewComments.adapter = commentAdapter

        viewModel.collector.observe(viewLifecycleOwner) { collector ->
            binding.collector = collector
            (activity as? AppCompatActivity)?.supportActionBar?.title = collector.name

            performerAdapter.updatePerformers(collector.favoritePerformers)

//            if (collector.albums.isEmpty()) {
//                binding.textViewNoAlbums.visibility = View.VISIBLE
//                binding.recyclerViewAlbums.visibility = View.GONE
//            } else {
//                binding.textViewNoAlbums.visibility = View.GONE
//                binding.recyclerViewAlbums.visibility = View.VISIBLE
//                albumAdapter.updateAlbums(collector.albums)
//            }

            if (collector.comments.isEmpty()) {
                binding.textViewNoComments.visibility = View.VISIBLE
                binding.recyclerViewComments.visibility = View.GONE
            } else {
                binding.textViewNoComments.visibility = View.GONE
                binding.recyclerViewComments.visibility = View.VISIBLE
                commentAdapter.updateComments(collector.comments)
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
        Toast.makeText(activity, "Error de red al cargar el coleccionista", Toast.LENGTH_LONG).show()
        viewModel.onNetworkErrorShown()
    }
}
