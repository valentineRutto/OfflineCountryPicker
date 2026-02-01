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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.valentinerutto.offlinecountrypicker.data.model.CountryDataProvider
import com.valentinerutto.offlinecountrypickerLibray.R


@Composable
fun CountryCodePickerUI (
    selectedCountry: Country? = CountryDataProvider.getCountryByCode("KE"),
    onCountrySelected:(Country) -> Unit,
    modifier: Modifier = Modifier,
    repository: CountryRepository = remember { CountryRepository() }

){
    var showDialog by remember { mutableStateOf(false) }
    val displayCountry by remember { mutableStateOf(selectedCountry) }

    OutlinedButton(onClick = { showDialog = true }, modifier = modifier, shape = RoundedCornerShape(8.dp)) {
     displayCountry?.let {
     Text(text = it.flag , fontSize = 24.sp, modifier = Modifier.padding(end = 4.dp))
        Text(text = it.dialCode )

         Icon(imageVector = Icons.Default.Search , contentDescription = "Search")
    }
    }

    if (showDialog){
        CountryPickerDialog(
            onDismiss = { showDialog = false },
            onCountrySelected = {
                onCountrySelected(it)
                showDialog = false
            },
            repository = repository
        )

    }


}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CountryPickerDialog(
    onDismiss: () -> Unit,
    onCountrySelected: (Country) -> Unit,
    repository: CountryRepository
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
                            onClick = { onCountrySelected(country) }
                        )
                    }

                }

            }

        }}}
@Composable
fun CountryListItem(
    country: Country,
    onClick: () -> Unit,
    showDialCode: Boolean = true,
    showFlag: Boolean = true,
    showContinet: Boolean = true,
    showLanguage: Boolean = true,
    showCurrency: Boolean = true,
    showCapital: Boolean = true,
    showISO: Boolean = true,
    itemPadding:Int = 10,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(itemPadding.dp, (itemPadding * 1.5).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val countryString = if (showFlag && showISO){
            "${country.flag} ${country.code}"

        }else if (showFlag){
            country.displayflag
        }else if (showISO){
            country.name + " " + country.code

        }else{
            country.name
        }


        Text(
            text = country.flag,
            fontSize = 32.sp,
            modifier = Modifier.padding(end = 12.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = country.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = country.dialCode,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

      CountryPickerScreen( onCountrySelected = onCountrySelected, onDismiss = {}, showRecentlyUsed = true, recentlyUsedCountries = CountryDataProvider.getAllCountries().take(3))
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

//    CountryCodePickerUI(selectedCountry = Country("KE","Kenya","+254","🇰🇪","KES","Africa","Nairobi",listOf("English","Swahili")), onCountrySelected = {
//
//    })

    CountryListItem(Country("KE", "Kenya", "+254", "🇰🇪", "KES", "Africa", "Nairobi", listOf("English", "Swahili")), onClick = { /*TODO*/ })
}
