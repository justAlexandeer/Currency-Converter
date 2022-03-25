package com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.github.justalexandeer.currencyconverter.business.domain.model.Currency
import com.github.justalexandeer.currencyconverter.business.domain.model.CurrencyViewHolderState
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.view.CurrencyViewHolder

class CurrencyAdapter(private val onCurrencyClickListener: OnCurrencyClickListener) :
    ListAdapter<CurrencyViewHolderState, CurrencyViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onCurrencyClickListener)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<CurrencyViewHolderState>() {
            override fun areItemsTheSame(
                oldItem: CurrencyViewHolderState,
                newItem: CurrencyViewHolderState
            ): Boolean =
                oldItem.currency.id == newItem.currency.id

            override fun areContentsTheSame(
                oldItem: CurrencyViewHolderState,
                newItem: CurrencyViewHolderState
            ): Boolean =
                oldItem == newItem
        }
    }
}