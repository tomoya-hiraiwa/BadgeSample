package com.example.badgequestion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.badgequestion.databinding.FragmentListBinding
import kotlinx.coroutines.launch
import java.io.File


class ListFragment : Fragment() {
 private var _binding:FragmentListBinding? = null
    private lateinit var viewModel: ListViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ListViewModel::class.java]
        val list = binding.listList
        val file = File(requireContext().filesDir,"events_data.json")
        list.layoutManager = GridLayoutManager(requireContext(),2)
        viewModel.listData.observe(viewLifecycleOwner){data ->
            val adapter = ListAdapter(data,requireContext())
            list.adapter = adapter
            adapter.setOnListClicker(object: ListAdapter.ListClicker{
                override fun onClick(data: EventData) {
                    lifecycleScope.launch {
                        viewModel.changeStatus(file, data)
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}