package com.example.currencyexchangeapp.data

import com.example.currencyexchangeapp.models.ConvertCurrency
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val currencyRatesAPI: CurrencyRatesAPI) {

    suspend fun getExchangeRates(queries: Map<String, String>): Response<ConvertCurrency> {
        return currencyRatesAPI.getExchangeRates(queries)
    }
}