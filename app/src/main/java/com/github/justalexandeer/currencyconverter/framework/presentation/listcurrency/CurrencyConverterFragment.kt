package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.justalexandeer.currencyconverter.databinding.FragmentListCurrencyBinding
import com.github.justalexandeer.currencyconverter.framework.presentation.CurrencyConverterApplication
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenState
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view.recyclerview.CurrencyAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyConverterFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<CurrencyConverterViewModel> { viewModelFactory }

    private var _binding: FragmentListCurrencyBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as CurrencyConverterApplication).appComponent.currencyConverterComponent()
            .create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CurrencyAdapter()
        binding.listCurrencyRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listCurrencyRecyclerView.adapter = adapter

        binding.button.setOnClickListener {
            viewModel.getCurrency()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currencyListScreenState.collect {
                it.data?.let { list ->
                    adapter.submitList(list)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}