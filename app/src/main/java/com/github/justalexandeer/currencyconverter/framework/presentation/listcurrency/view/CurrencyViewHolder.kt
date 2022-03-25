package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.github.justalexandeer.currencyconverter.business.domain.model.CurrencyViewHolderState
import com.github.justalexandeer.currencyconverter.business.domain.util.valueCurrency
import com.github.justalexandeer.currencyconverter.databinding.CurrencyViewItemBinding
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view.recyclerview.OnCurrencyClickListener

class CurrencyViewHolder(
    private val binding: CurrencyViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        currencyViewHolderState: CurrencyViewHolderState,
        onCurrencyClickListener: OnCurrencyClickListener
    ) {
        binding.currencyItem.setOnClickListener {
            onCurrencyClickListener.onCurrencyClick(currencyViewHolderState.currency)
        }
        binding.currencyItemLeftBorder.isVisible = currencyViewHolderState.isBorderVisible
        currencyViewHolderState.currency.also {
            binding.currencyItemCharCode.text = it.charCode
            binding.currencyItemName.text = it.name

            val itemValueText = "${valueCurrency(it.value / it.nominal)} ₽"
            binding.currencyItemValue.text = itemValueText
        }
    }

    companion object {
        fun create(parent: ViewGroup): CurrencyViewHolder {
            val binding = CurrencyViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CurrencyViewHolder(binding)
        }
    }
}