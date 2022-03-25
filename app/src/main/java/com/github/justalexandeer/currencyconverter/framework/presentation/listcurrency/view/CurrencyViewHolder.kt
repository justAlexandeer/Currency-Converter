package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.databinding.CurrencyViewItemBinding

class CurrencyViewHolder(
    private val binding: CurrencyViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(currency: Currency) {
        binding.currencyItemCharCode.text = currency.charCode
        binding.currencyItemName.text = currency.name
        val itemValueText = "${(currency.value / currency.nominal).toString().substring(0, 6)} ₽"
        binding.currencyItemValue.text = itemValueText
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