package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.github.justalexandeer.currencyconverter.databinding.FragmentListCurrencyBinding
import com.github.justalexandeer.currencyconverter.framework.presentation.CurrencyConverterApplication
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
        binding.button.setOnClickListener {
            viewModel.getCurrency()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}