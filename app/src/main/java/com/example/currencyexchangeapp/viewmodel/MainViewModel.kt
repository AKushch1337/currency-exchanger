package com.example.currencyexchangeapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyexchangeapp.data.Repository
import com.example.currencyexchangeapp.models.ConvertCurrency
import com.example.currencyexchangeapp.models.Rates
import com.example.currencyexchangeapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application,
) : AndroidViewModel(application) {

    var exchangeRatesResponse: MutableLiveData<NetworkResult<ConvertCurrency>> = MutableLiveData()

    fun getExchangeRates(queries: Map<String, String>) {
        viewModelScope.launch {
            getExchangeRatesSafeCall(queries)
        }
    }

    private suspend fun getExchangeRatesSafeCall(queries: Map<String, String>) {
        if (checkInternetConnection()) {
            try {
                val response = repository.remote.getExchangeRates(queries)
                exchangeRatesResponse.value = handleExchangeRateResponse(response)
            } catch (e: Exception) {
                exchangeRatesResponse.value = NetworkResult.Error(message = "NO RESPONSE")
            }
        } else {
            exchangeRatesResponse.value =
                NetworkResult.Error(message = "NO INTERNET. CHECK YOUR CONNECTION")
        }

    }

    private fun handleExchangeRateResponse(response: Response<ConvertCurrency>): NetworkResult<ConvertCurrency> {
        return when {
            response.message().toString().contains("timeout", true) -> {
                NetworkResult.Error(message = "TIMEOUT")
            }
            response.isSuccessful -> {
                val exchangeResponse = response.body()
                NetworkResult.Success(data = exchangeResponse!!)
            }
            else -> {
                NetworkResult.Error(message = "COULDN'T FETCH DATA")
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false


        }

    }
    fun getToValue(currencyCode: String, rates: Rates): Double {
        return when (currencyCode) {
            "AUD" -> rates.aUD
            "BRL" -> rates.bRL
            "BGN" -> rates.bGN
            "CAD" -> rates.cAD
            "CNY" -> rates.cNY
            "HRK" -> rates.hRK
            "CZK" -> rates.cZK
            "DKK" -> rates.dKK
            "EUR" -> rates.eUR
            "GBP" -> rates.gBP
            "HKD" -> rates.hKD
            "HUF" -> rates.hUF
            "ISK" -> rates.iSK
            "INR" -> rates.iNR
            "IDR" -> rates.iDR
            "ILS" -> rates.iLS
            "JPY" -> rates.jPY
            "KRW" -> rates.kRW
            "MYR" -> rates.mYR
            "MXN" -> rates.mXN
            "NZD" -> rates.nZD
            "NOK" -> rates.nOK
            "PHP" -> rates.pHP
            "PLN" -> rates.pLN
            "RON" -> rates.rON
            "SGD" -> rates.sGD
            "ZAR" -> rates.zAR
            "SEK" -> rates.sEK
            "CHF" -> rates.cHF
            "THB" -> rates.tHB
            "TRY" -> rates.tRY
            "USD" -> rates.uSD
            "UAH" -> rates.uAH
            else -> 0.00
        }
    }

    fun provideQueries(from: String): HashMap<String, String> {
        val queries = HashMap<String, String>()
        queries["base"] = from
        return queries
    }

    fun getOutputString(value: Double): String {
        return "%.2f".format(value)
    }
}