package com.github.justalexandeer.currencyconverter.framework.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.github.justalexandeer.currencyconverter.R
import com.github.justalexandeer.currencyconverter.databinding.ActivityMainBinding
import com.github.justalexandeer.currencyconverter.framework.presentation.listcurrency.CurrencyConverterFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CurrencyConverterFragment>(R.id.fragment_container_view)
            }
        }
    }
}