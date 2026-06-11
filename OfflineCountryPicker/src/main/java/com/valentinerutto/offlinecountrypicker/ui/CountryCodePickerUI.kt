package com.valentinerutto.offlinecountrypicker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.valentinerutto.offlinecountrypicker.data.CountryRepository
import com.valentinerutto.offlinecountrypicker.data.model.Country
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.valentinerutto.offlinecountrypicker.data.model.CountryDataProvider

enum class CountryDisplayOption {
    FLAG,
    CODE,
    NAME,
    DIAL_CODE,
    CURRENCY,
    LANGUAGES,
    CAPITAL
}

enum class CountryPickerPresentation {
    MODAL_BOTTOM_SHEET,
    DIALOG
}

object CountryDisplayDefaults {
    val Flag = setOf(CountryDisplayOption.FLAG)

    val Code = setOf(CountryDisplayOption.CODE)

    val Name = setOf(CountryDisplayOption.NAME)

    val DialCode = setOf(CountryDisplayOption.DIAL_CODE)

    val Currency = setOf(CountryDisplayOption.CURRENCY)

    val Languages = setOf(CountryDisplayOption.LANGUAGES)

    val Capital = setOf(CountryDisplayOption.CAPITAL)

    val FlagAndDialCode = setOf(
        CountryDisplayOption.FLAG,
        CountryDisplayOption.DIAL_CODE
    )

    val FlagAndName = setOf(
        CountryDisplayOption.FLAG,
        CountryDisplayOption.NAME
    )

    val NameAndDialCode = setOf(
        CountryDisplayOption.NAME,
        CountryDisplayOption.DIAL_CODE
    )

    val FlagNameAndCurrency = setOf(
        CountryDisplayOption.FLAG,
        CountryDisplayOption.NAME,
        CountryDisplayOption.CURRENCY
    )

    val FlagNameCodeAndDialCode = setOf(
        CountryDisplayOption.FLAG,
        CountryDisplayOption.NAME,
        CountryDisplayOption.CODE,
        CountryDisplayOption.DIAL_CODE
    )

    val NameAndCurrency = setOf(
        CountryDisplayOption.NAME,
        CountryDisplayOption.CURRENCY
    )

    val FlagDialCodeAndCurrency = setOf(
        CountryDisplayOption.FLAG,
        CountryDisplayOption.DIAL_CODE,
        CountryDisplayOption.CURRENCY
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodePickerUI(
    selectedCountry: Country? = CountryDataProvider.getCountryByCode("KE"),
    onCountrySelected: (Country) -> Unit,
    modifier: Modifier = Modifier,
    repository: CountryRepository = remember { CountryRepository() },
    onCurrencySelected: ((String?) -> Unit)? = null,
    selectedCountryDisplayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.FlagAndDialCode,
    pickerItemDisplayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.FlagNameCodeAndDialCode,
    pickerPresentation: CountryPickerPresentation = CountryPickerPresentation.MODAL_BOTTOM_SHEET
) {
    var showCountryPicker by remember { mutableStateOf(false) }
    var displayCountry by remember { mutableStateOf(selectedCountry) }

    LaunchedEffect(selectedCountry) {
        displayCountry = selectedCountry
    }

    var recentCountries by remember {
        mutableStateOf(CountryDataProvider.countries.take(3))
    }

    OutlinedButton(
        onClick = { showCountryPicker = true },
        modifier = modifier,
        shape = RoundedCornerShape(8.dp)
    ) {
        displayCountry?.let { country ->
            CountryDisplayText(
                country = country,
                displayOptions = selectedCountryDisplayOptions
            )
        }
    }

    if (showCountryPicker) {
        val dismissPicker = { showCountryPicker = false }
        val selectCountry: (Country) -> Unit = { country ->
            displayCountry = country
            onCountrySelected(country)
            onCurrencySelected?.invoke(country.currency)

            recentCountries = (listOf(country) + recentCountries)
                .distinctBy { it.code }
                .take(3)

            dismissPicker()
        }

        when (pickerPresentation) {
            CountryPickerPresentation.MODAL_BOTTOM_SHEET -> {
                ModalBottomSheet(
                    onDismissRequest = dismissPicker,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    CountryPickerScreen(
                        onCountrySelected = selectCountry,
                        onDismiss = dismissPicker,
                        recentlyUsedCountries = recentCountries,
                        showRecentlyUsed = true,
                        itemDisplayOptions = pickerItemDisplayOptions,
                        countries = repository.getAllCountries()
                    )
                }
            }

            CountryPickerPresentation.DIALOG -> {
                CountryPickerDialog(
                    onDismiss = dismissPicker,
                    onCountrySelected = selectCountry,
                    repository = repository,
                    recentlyUsedCountries = recentCountries,
                    showRecentlyUsed = true,
                    itemDisplayOptions = pickerItemDisplayOptions,
                    countries = repository.getAllCountries()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPickerDialog(
    onDismiss: () -> Unit,
    onCountrySelected: (Country) -> Unit,
    repository: CountryRepository = remember { CountryRepository() },
    modifier: Modifier = Modifier,
    recentlyUsedCountries: List<Country> = emptyList(),
    showRecentlyUsed: Boolean = true,
    itemDisplayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.FlagNameCodeAndDialCode,
    countries: List<Country> = repository.getAllCountries()
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            CountryPickerScreen(
                onCountrySelected = onCountrySelected,
                onDismiss = onDismiss,
                modifier = modifier,
                recentlyUsedCountries = recentlyUsedCountries,
                showRecentlyUsed = showRecentlyUsed,
                itemDisplayOptions = itemDisplayOptions,
                countries = countries
            )
        }
    }
}
@Composable
fun CountryListItem(
    country: Country,
    onClick: () -> Unit,
    displayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.FlagNameCodeAndDialCode,
    itemPadding: Int = 10,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(itemPadding.dp, (itemPadding * 1.5).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountryDisplayText(
            country = country,
            displayOptions = displayOptions,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CountryDisplayText(
    country: Country,
    displayOptions: Set<CountryDisplayOption>,
    modifier: Modifier = Modifier
) {
    val options = displayOptions.ifEmpty {
        CountryDisplayDefaults.FlagNameAndCurrency
    }

    val hasName = CountryDisplayOption.NAME in options
    val primaryParts = buildList {
        if (CountryDisplayOption.FLAG in options) add(country.flag)
        if (hasName) add(country.name)
        if (!hasName && CountryDisplayOption.CODE in options) add(country.code)
        if (!hasName && CountryDisplayOption.DIAL_CODE in options) add(country.dialCode)
        if (!hasName && CountryDisplayOption.CURRENCY in options) country.currency?.let(::add)
    }
    val detailParts = buildList {
        if (hasName && CountryDisplayOption.CODE in options) add(country.code)
        if (hasName && CountryDisplayOption.DIAL_CODE in options) add(country.dialCode)
        if (hasName && CountryDisplayOption.CURRENCY in options) country.currency?.let(::add)
        if (CountryDisplayOption.CAPITAL in options) country.capital?.let { add("Capital: $it") }
    }
    val languageText = if (CountryDisplayOption.LANGUAGES in options && !country.languages.isNullOrEmpty()) {
        "Languages: ${country.languages.joinToString(", ")}"
    } else {
        null
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (primaryParts.isNotEmpty()) {
            Text(
                text = primaryParts.joinToString(" "),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (hasName) FontWeight.Medium else FontWeight.Normal,
                softWrap = true
            )
        }

        if (detailParts.isNotEmpty()) {
            Text(
                text = detailParts.joinToString(" · "),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                softWrap = true
            )
        }

        if (languageText != null) {
            Text(
                text = languageText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                softWrap = true
            )
        }
    }
}


@Preview
@Composable
fun PreviewUI(){


    CountryListItem(Country("KE", "Kenya", "+254", "🇰🇪", "KES", "Africa", "Nairobi", listOf("English", "Swahili")), onClick = { /*TODO*/ })
}
