package com.valentinerutto.offlinecountrypicker.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinerutto.offlinecountrypicker.data.CountryRepository
import com.valentinerutto.offlinecountrypicker.data.model.Country
import com.valentinerutto.offlinecountrypicker.data.model.CountryDataProvider
import com.valentinerutto.offlinecountrypicker.ui.CountryDisplayDefaults
import com.valentinerutto.offlinecountrypicker.ui.PhoneNumberInput
import com.valentinerutto.offlinecountrypicker.sample.ui.theme.OfflineCountryPickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfflineCountryPickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    CountryPickerSampleScreen(
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

@Composable
fun CountryPickerSampleScreen(
    modifier: Modifier = Modifier,
    repository: CountryRepository = remember { CountryRepository() }
) {

    var selectedCountry by remember {
        mutableStateOf(CountryDataProvider.getCountryByCode("KE"))
    }
    var selectedCurrency by remember {
        mutableStateOf(selectedCountry?.currency)
    }
    var phoneNumber by remember { mutableStateOf("") }
    var submittedNumber by remember { mutableStateOf<String?>(null) }

    val fullNumber = selectedCountry?.let { country ->
        "${country.dialCode}$phoneNumber"
    }.orEmpty()

    val parsedPhoneNumber = remember(fullNumber) {
        repository.parsePhoneNumber(fullNumber)
    }

    val isPhoneNumberInvalid = phoneNumber.isNotEmpty() &&
        parsedPhoneNumber?.isValid != true

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Offline Country Picker",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Select a country code and enter a local phone number.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        PhoneNumberInput(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = {
                phoneNumber = it
                submittedNumber = null
            },
            selectedCountry = selectedCountry,
            onCountrySelected = {
                selectedCountry = it
                selectedCurrency = it.currency
                submittedNumber = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = "Phone number",
            isError = isPhoneNumberInvalid,
            errorMessage = "Enter 7 to 15 digits",
            onCurrencySelected = { selectedCurrency = it },
            countryPickerDisplayOptions = CountryDisplayDefaults.FlagDialCodeAndCurrency,
            countryPickerItemDisplayOptions = CountryDisplayDefaults.All
        )

        Button(
            onClick = { submittedNumber = fullNumber },
            enabled = parsedPhoneNumber?.isValid == true,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }

        SelectedCountryCard(
            country = selectedCountry,
            currency = selectedCurrency,
            fullNumber = submittedNumber ?: fullNumber.takeIf { phoneNumber.isNotEmpty() }
        )
    }
}

@Composable
private fun SelectedCountryCard(
    country: Country?,
    currency: String?,
    fullNumber: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Selected country",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = country?.let { "${it.flag} ${it.name} (${it.dialCode})" }
                    ?: "No country selected",
                style = MaterialTheme.typography.bodyLarge
            )

            if (currency != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Currency: $currency",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            country?.capital?.let { capital ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Capital: $capital",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (!country?.languages.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Languages: ${country.languages?.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (fullNumber != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Full number: $fullNumber",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountryPickerSampleScreenPreview() {
    OfflineCountryPickerTheme {
        CountryPickerSampleScreen()
    }
}
