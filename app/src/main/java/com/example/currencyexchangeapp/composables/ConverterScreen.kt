package com.example.currencyexchangeapp.composables

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.*
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.example.currencyexchangeapp.models.Rates
import com.example.currencyexchangeapp.ui.theme.Purple500
import com.example.currencyexchangeapp.utils.Constants.Companion.BAR_TITLE
import com.example.currencyexchangeapp.utils.Constants.Companion.CURRENCY_CODES_LIST
import com.example.currencyexchangeapp.utils.NetworkResult
import com.example.currencyexchangeapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ConverterScreen(context: Context, mainViewModel: MainViewModel) {

    val fromCurrencyCode = remember {
        mutableStateOf("UAH")
    }
    val toCurrencyCode = remember {
        mutableStateOf("SEK")
    }
    val amountValue = remember {
        mutableStateOf("")
    }
    val convertedAmount = remember {
        mutableStateOf("")
    }
    val singleConvertedValue = remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    val isDarkMode: Boolean = isSystemInDarkTheme()

    val scaffoldState = rememberBottomSheetScaffoldState()

    var isFromSelected = true

    BottomSheetScaffold(
        sheetContent = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(275.dp)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    items(CURRENCY_CODES_LIST) { item ->
                        Text(text = "${item.currencyCode}\t ${item.countryName}",
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .clickable {
                                    if (isFromSelected) {
                                        fromCurrencyCode.value = item.currencyCode
                                    } else {
                                        toCurrencyCode.value = item.currencyCode
                                    }
                                    scope.launch { scaffoldState.bottomSheetState.collapse() }
                                })
                    }
                }
            }
        },
        topBar = {
            Spacer(modifier = Modifier.padding(15.dp))
            TopAppBar(
                title = {
                    Text(
                        text = BAR_TITLE,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 35.sp,
                        textAlign = TextAlign.Center,
                        color = if (isDarkMode) Color.White else Color.Black,
                        style = TextStyle(fontWeight = FontWeight.Black))
                },
                backgroundColor = if (isDarkMode) Color.Black else Color.White, elevation = 0.dp)
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetElevation = 6.dp,
        sheetBackgroundColor = Color(0xFFF1F1F1),
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)) {
        Column(modifier = Modifier
            .padding(50.dp)
            .fillMaxSize()) {
            Text(text = "FROM", color = Purple500)
            Spacer(modifier = Modifier.padding(5.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    isFromSelected = true
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                }
                .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.CenterStart) {
                Text(text = fromCurrencyCode.value, modifier = Modifier.padding(10.dp))
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "TO", color = Purple500)
            Spacer(modifier = Modifier.padding(5.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    isFromSelected = false
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                }
                .border(1.dp, Color.Black, RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.CenterStart) {
                Text(text = toCurrencyCode.value, modifier = Modifier.padding(10.dp))
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "AMOUNT", color = Purple500)
                Text(text = fromCurrencyCode.value, color = Purple500)

            }

            Spacer(modifier = Modifier.padding(5.dp))

            OutlinedTextField(
                value = amountValue.value,
                onValueChange = {
                    amountValue.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                placeholder = {
                    Text(text = "0.00")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.padding(15.dp))

            Button(
                onClick = {
                    mainViewModel.getExchangeRates(mainViewModel.provideQueries(fromCurrencyCode.value))
                    mainViewModel.exchangeRatesResponse.observe(context as LifecycleOwner) { response ->
                        when (response) {
                            is NetworkResult.Success -> {
                                response.data?.let {
                                    if (amountValue.value.isEmpty()) {
                                        amountValue.value = "1.00"
                                    }
                                    val toValue =
                                        mainViewModel.getToValue(toCurrencyCode.value, it.rates)
                                    val amount = amountValue.value.toDouble()
                                    convertedAmount.value =
                                        "${mainViewModel.getOutputString(amount * toValue)} ${toCurrencyCode.value}"
                                    singleConvertedValue.value =
                                        "1 ${fromCurrencyCode.value} = ${
                                            mainViewModel.getOutputString(toValue)
                                        } ${toCurrencyCode.value}"
                                }
                            }
                            is NetworkResult.Error -> {
                                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            }
                            is NetworkResult.Loading -> {
                                Toast.makeText(context, "LOADING", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp))
            {
                Text(text = "CONVERT", fontSize = 25.sp)
            }
            Spacer(modifier = Modifier.padding(15.dp))
            Text(
                text = convertedAmount.value,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 30.sp,
                color = Color.Black,
                style = TextStyle(textAlign = TextAlign.Center))
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = singleConvertedValue.value,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 30.sp,
                color = Color.Black,
                style = TextStyle(textAlign = TextAlign.Center))
        }
    }
}