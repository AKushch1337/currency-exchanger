package com.example.currencyexchangeapp.models


import com.google.gson.annotations.SerializedName

data class ConvertCurrency(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Rates,
)