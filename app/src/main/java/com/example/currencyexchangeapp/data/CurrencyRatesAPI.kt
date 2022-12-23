package com.example.currencyexchangeapp.data

import com.example.currencyexchangeapp.models.ConvertCurrency
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CurrencyRatesAPI {

    @GET("latest")
    suspend fun getExchangeRates(@QueryMap queries: Map<String, String>): Response<ConvertCurrency>
}