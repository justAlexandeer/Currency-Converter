package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.justalexandeer.currencyconverter.R
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.util.fromLongToDate
import com.github.justalexandeer.currencyconverter.business.domain.util.valueCurrency
import com.github.justalexandeer.currencyconverter.databinding.FragmentListCurrencyBinding
import com.github.justalexandeer.currencyconverter.framework.presentation.CurrencyConverterApplication
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.model.CurrencyListScreenEvent
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view.recyclerview.CurrencyAdapter
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view.recyclerview.OnCurrencyClickListener
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

        binding.converterLayout.converterEditText.addTextChangedListener {
            viewModel.obtainEvent(CurrencyListScreenEvent.ConvertValueEditTextChange(it.toString()))
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.getCurrency()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currencyListScreenState.collect { it ->
                it.data?.let { list ->
                    adapter.submitList(list)
                }
                if(it.chosenCurrency != null) {
                    binding.converterLayout.converterCurrencyTextView.text = it.chosenCurrency.name
                } else {
                    binding.converterLayout.converterCurrencyTextView.text =
                        activity?.resources?.getString(R.string.converter_text_view_currency)
                }
                if (it.convertResult != null) {
                    binding.converterLayout.converterCurrencyResultTextView.text = it.convertResult
                    it.chosenCurrency?.let { currency ->
                        binding.converterLayout.converterCurrencyResultCharCodeTextView.text =
                            currency.charCode
                    }
                } else {
                    binding.converterLayout.converterCurrencyResultTextView.text = ""
                    binding.converterLayout.converterCurrencyResultCharCodeTextView.text = ""
                }
                it.isLoading.let {
                    if (!it)
                        binding.swiperefresh.isRefreshing = it
                }
                it.lastUpdate?.let {
                    var dateString =
                        activity?.resources?.getString(R.string.list_currency_last_update)
                    dateString = dateString?.plus(" ${fromLongToDate(it)}")
                    binding.listCurrencyLastUpdate.text = dateString
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
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