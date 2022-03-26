package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view

import android.graphics.Color
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

            val itemValueText = "${valueCurrency(it.value / it.nominal, 6)} ₽"
            binding.currencyItemValue.text = itemValueText
            val itemValueChange = it.value - it.previous
            if(itemValueChange > 0) {
                val itemValueChangeText = "+${valueCurrency(itemValueChange, 4)} ₽"
                binding.currencyItemValueChange.setTextColor(Color.GREEN)
                binding.currencyItemValueChange.text = itemValueChangeText
            } else {
                val itemValueChangeText = "${valueCurrency(itemValueChange, 5)} ₽"
                binding.currencyItemValueChange.setTextColor(Color.RED)
                binding.currencyItemValueChange.text = itemValueChangeText
            }
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