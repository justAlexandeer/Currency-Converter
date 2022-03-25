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
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.util.valueCurrency
import com.github.justalexandeer.currencyconverter.databinding.FragmentListCurrencyBinding
import com.github.justalexandeer.currencyconverter.framework.presentation.CurrencyConverterApplication
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenEvent
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenState
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view.recyclerview.CurrencyAdapter
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view.recyclerview.OnCurrencyClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyConverterFragment : Fragment(), OnCurrencyClickListener {

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
        val adapter = CurrencyAdapter(this)
        binding.listCurrencyRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.listCurrencyRecyclerView.adapter = adapter
        binding.button.setOnClickListener {
            viewModel.getCurrency()
        }
        binding.converterLayout.convertButton.setOnClickListener {
            viewModel.obtainEvent(
                CurrencyListScreenEvent.OnConvertButtonClick(
                    binding.converterLayout.converterEditText.text.toString()
                )
            )
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currencyListScreenState.collect { it ->
                it.data?.let { list ->
                    adapter.submitList(list)
                }
                it.chosenCurrency?.let { currency ->
                    binding.converterLayout.converterCurrencyTextView.text = currency.name
                }
                it.convertResult?.let { result ->
                    var itemValueText = valueCurrency(result)
                    it.chosenCurrency?.let {
                        if (result.isNotBlank()) {
                            itemValueText = "$itemValueText ${it.charCode}"
                        }
                    }
                    binding.converterLayout.converterCurrencyResultTextView.text = itemValueText
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCurrencyClick(currency: Currency) {
        viewModel.obtainEvent(CurrencyListScreenEvent.OnCurrencyClick(currency))
    }
}