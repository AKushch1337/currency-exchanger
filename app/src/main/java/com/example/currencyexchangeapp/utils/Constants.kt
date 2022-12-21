package com.example.currencyexchangeapp.utils

import com.example.currencyexchangeapp.models.CurrencyCountry

class Constants {
    companion object {

        const val BAR_TITLE = "Currency Converter"

        const val BASE_URL = "https://api.exchangerate.host/"

        val CURRENCY_CODES_LIST = listOf(
                CurrencyCountry("Australia", "AUD"),
                CurrencyCountry("Brazil", "BRL"),
                CurrencyCountry("Bulgaria", "BGN"),
                CurrencyCountry("Canada", "CAD"),
                CurrencyCountry("China", "CNY"),
                CurrencyCountry("Croatia", "HRK"),
                CurrencyCountry("Czech Republic", "CZK"),
                CurrencyCountry("Denmark", "DKK"),
                CurrencyCountry("European Union", "EUR"),
                CurrencyCountry("Great Britain", "GBP"),
                CurrencyCountry("Hong Kong", "HKD"),
                CurrencyCountry("Hungary", "HUF"),
                CurrencyCountry("Iceland", "ISK"),
                CurrencyCountry("India", "INR"),
                CurrencyCountry("Indonesia", "IDR"),
                CurrencyCountry("Israel", "ILS"),
                CurrencyCountry("Japan", "JPY"),
                CurrencyCountry("Korea", "KRW"),
                CurrencyCountry("Malaysia", "MYR"),
                CurrencyCountry("Mexico", "MXN"),
                CurrencyCountry("New Zealand", "NZD"),
                CurrencyCountry("Norway", "NOK"),
                CurrencyCountry("Philippines", "PHP"),
                CurrencyCountry("Poland", "PLN"),
                CurrencyCountry("Romania", "RON"),
                CurrencyCountry("Singapore", "SGD"),
                CurrencyCountry("South Africa", "ZAR"),
                CurrencyCountry("Sweden", "SEK"),
                CurrencyCountry("Switzerland", "CHF"),
                CurrencyCountry("Thailand", "THB"),
                CurrencyCountry("Turkey", "TRY"),
                CurrencyCountry("United States", "USD"),
                CurrencyCountry("Ukraine", "UAH"),
                )
    }
}