package com.valentinerutto.offlinecountrypicker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

object CountryDisplayDefaults {
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

    val All = setOf(
        CountryDisplayOption.FLAG,
        CountryDisplayOption.CODE,
        CountryDisplayOption.NAME,
        CountryDisplayOption.DIAL_CODE,
        CountryDisplayOption.CURRENCY,
        CountryDisplayOption.LANGUAGES,
        CountryDisplayOption.CAPITAL
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
    pickerItemDisplayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.All
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
        ModalBottomSheet(
            onDismissRequest = { showCountryPicker = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            CountryPickerScreen(
                onCountrySelected = { country ->
                    displayCountry = country
                    onCountrySelected(country)
                    onCurrencySelected?.invoke(country.currency)

                    recentCountries = (listOf(country) + recentCountries)
                        .distinctBy { it.code }
                        .take(3)

                    showCountryPicker = false
                },
                onDismiss = { showCountryPicker = false },
                recentlyUsedCountries = recentCountries,
                showRecentlyUsed = true,
                itemDisplayOptions = pickerItemDisplayOptions,
                countries = repository.getAllCountries()
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CountryPickerDialog(
    onDismiss: () -> Unit,
    onCountrySelected: (Country) -> Unit,
    repository: CountryRepository,
    itemDisplayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.All
) {


var query by remember { mutableStateOf("") }

    var countries by remember { mutableStateOf(listOf<Country>()) }

    LaunchedEffect(query) {

     countries =   if (query.isEmpty()) {
            repository.getAllCountries()
        } else {
            repository.searchCountries(query)
        }
    }

    Dialog(onDismissRequest = onDismiss ) {

        Surface(shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Select Country",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                            fontSize = 17.sp,
                            color = Color(0xFF007AFF)
                        )
                    }
                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search countries...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)

                    },
                    trailingIcon = {
                    },
                        singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {

                    items(countries){ country ->
                        CountryListItem(
                            country = country,
                            onClick = { onCountrySelected(country) },
                            displayOptions = itemDisplayOptions
                        )
                    }

                }

            }

        }}}
@Composable
fun CountryListItem(
    country: Country,
    onClick: () -> Unit,
    displayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.All,
    itemPadding:Int = 10,
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

@Composable
fun PhoneNumberInput(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    selectedCountry: Country?,
    onCountrySelected: (Country) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Phone Number",
    isError: Boolean = false,
    errorMessage: String? = null,
    onCurrencySelected: ((String?) -> Unit)? = null,
    countryPickerDisplayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.FlagAndDialCode,
    countryPickerItemDisplayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.All
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            CountryCodePickerUI(
                selectedCountry = selectedCountry,
                onCountrySelected = onCountrySelected,
                onCurrencySelected = onCurrencySelected,
                selectedCountryDisplayOptions = countryPickerDisplayOptions,
                pickerItemDisplayOptions = countryPickerItemDisplayOptions
            )

            Spacer(
                modifier = Modifier
                    .width(1.dp)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { value ->
                    // Only allow digits
                    if (value.all { it.isDigit() }) {
                        onPhoneNumberChange(value)
                    }
                },
                modifier = Modifier.weight(1f),
                label = { Text(label) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                isError = isError,
                supportingText = if (isError && errorMessage != null) {
                    { Text(errorMessage) }
                } else null
            )
        }

        if (selectedCountry != null && phoneNumber.isNotEmpty()) {

            Text(
                text = "Full number: ${selectedCountry.dialCode}$phoneNumber",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewUI(){


    CountryListItem(Country("KE", "Kenya", "+254", "🇰🇪", "KES", "Africa", "Nairobi", listOf("English", "Swahili")), onClick = { /*TODO*/ })
}
