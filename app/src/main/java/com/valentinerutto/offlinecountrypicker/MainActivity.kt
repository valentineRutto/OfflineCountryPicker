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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.valentinerutto.offlinecountrypicker.ui.CountryCodePickerUI
import com.valentinerutto.offlinecountrypicker.ui.CountryDisplayDefaults
import com.valentinerutto.offlinecountrypicker.ui.CountryDisplayOption
import com.valentinerutto.offlinecountrypicker.sample.ui.theme.OfflineCountryPickerTheme
import com.valentinerutto.offlinecountrypicker.ui.CountryPickerDialog

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
    modifier: Modifier = Modifier
) {
    var selectedCountry by remember {
        mutableStateOf(CountryDataProvider.getCountryByCode("KE"))
    }
    var selectedCurrency by remember {
        mutableStateOf(selectedCountry?.currency)
    }

    val displayShowcases = remember {
        listOf(
            DisplayShowcase(
                title = "Flag + dial code",
                options = CountryDisplayDefaults.FlagAndDialCode
            ),
            DisplayShowcase(
                title = "Flag + country name",
                options = CountryDisplayDefaults.FlagAndName
            ),
            DisplayShowcase(
                title = "Country name + dial code",
                options = CountryDisplayDefaults.NameAndDialCode
            ),
            DisplayShowcase(
                title = "Flag + name + currency",
                options = CountryDisplayDefaults.FlagNameAndCurrency
            ),
            DisplayShowcase(
                title = "Flag + code + name + dial code",
                options = CountryDisplayDefaults.FlagNameCodeAndDialCode
            ),
            DisplayShowcase(
                title = "Name + currency",
                options = CountryDisplayDefaults.NameAndCurrency
            ),
            DisplayShowcase(
                title = "Flag + dial code + currency",
                options = CountryDisplayDefaults.FlagDialCodeAndCurrency
            ),
            DisplayShowcase(
                title = "All display options",
                options = CountryDisplayDefaults.All
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Offline Country Picker",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        item {
            Text(
                text = "Standalone CountryCodePickerUI examples using each display option preset.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(displayShowcases) { showcase ->
            DisplayOptionExample(
                title = showcase.title,
                displayOptions = showcase.options,
                selectedCountry = selectedCountry,
                onCountrySelected = {
                    selectedCountry = it
                    selectedCurrency = it.currency
                },
                onCurrencySelected = { selectedCurrency = it }
            )
        }

        item {
            SelectedCountryCard(
                country = selectedCountry,
                currency = selectedCurrency
            )
        }
    }
}

private data class DisplayShowcase(
    val title: String,
    val options: Set<CountryDisplayOption>
)

@Composable
private fun DisplayOptionExample(
    title: String,
    displayOptions: Set<CountryDisplayOption>,
    selectedCountry: Country?,
    onCountrySelected: (Country) -> Unit,
    onCurrencySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )

        CountryCodePickerUI(
            selectedCountry = selectedCountry,
            onCountrySelected = onCountrySelected,
            modifier = Modifier.fillMaxWidth(),
            onCurrencySelected = onCurrencySelected,
            selectedCountryDisplayOptions = displayOptions,
            pickerItemDisplayOptions = CountryDisplayDefaults.All
        )
    }
}

@Composable
private fun SelectedCountryCard(
    country: Country?,
    currency: String?,
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
                text = country?.let { "${it.flag} ${it.name} (${it.code}) ${it.dialCode}" }
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
