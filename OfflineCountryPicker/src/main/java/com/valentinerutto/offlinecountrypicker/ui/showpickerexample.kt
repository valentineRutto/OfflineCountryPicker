package com.valentinerutto.offlinecountrypicker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinerutto.offlinecountrypicker.data.model.Country
import com.valentinerutto.offlinecountrypicker.data.model.CountryDataProvider


/**
 * Example usage of the CountryPickerScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showpickerexample() {
    var showCountryPicker by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var recentCountries by remember {
        mutableStateOf(CountryDataProvider.countries.take(3))
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { showCountryPicker = true }) {
                Text("Select Country")
            }

            selectedCountry?.let { country ->
                Card(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = country.flag,
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = country.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = country.dialCode,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

    if (showCountryPicker) {
        ModalBottomSheet(
            onDismissRequest = { showCountryPicker = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            CountryPickerScreen(
                onCountrySelected = { country ->
                    selectedCountry = country

                    // Update recently used countries
                    recentCountries = (listOf(country) + recentCountries)
                        .distinctBy { it.code }
                        .take(3)

                    showCountryPicker = false
                },
                onDismiss = { showCountryPicker = false },
                recentlyUsedCountries = recentCountries,
                showRecentlyUsed = true
            )
        }
    }
}

/**
 * Alternative usage as a full-screen destination
 */
@Composable
fun CountryPickerFullScreenExample(
    onCountrySelected: (Country) -> Unit,
    onDismiss: () -> Unit
) {
    CountryPickerScreen(
        onCountrySelected = onCountrySelected,
        onDismiss = onDismiss,
        recentlyUsedCountries =CountryDataProvider.countries.take(3),
        showRecentlyUsed = true
    )
}