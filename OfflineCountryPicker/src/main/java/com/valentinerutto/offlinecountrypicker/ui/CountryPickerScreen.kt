package com.valentinerutto.offlinecountrypicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinerutto.offlinecountrypicker.data.model.Country
import com.valentinerutto.offlinecountrypicker.data.model.CountryDataProvider.getAllCountries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPickerScreen(
    onCountrySelected: (Country) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    recentlyUsedCountries: List<Country> = emptyList(),
    showRecentlyUsed: Boolean = true,
    itemDisplayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.All,
    countries: List<Country> = getAllCountries()
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredCountries = remember(countries, searchQuery) {
        if (searchQuery.isEmpty()) {
            countries
        } else {
            countries.filter { country ->
                country.name.contains(searchQuery, ignoreCase = true) ||
                        country.code.contains(searchQuery, ignoreCase = true) ||
                        country.dialCode.contains(searchQuery, ignoreCase = true) ||
                        country.currency?.contains(searchQuery, ignoreCase = true) == true ||
                        country.capital?.contains(searchQuery, ignoreCase = true) == true ||
                        country.languages?.any { language ->
                            language.contains(searchQuery, ignoreCase = true)
                        } == true
            }
        }
    }

    val groupedCountries = remember(filteredCountries) {
        filteredCountries.groupBy { it.name.first().uppercaseChar() }
            .toSortedMap()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Select Country",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            navigationIcon = {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Cancel",
                        fontSize = 17.sp,
                        color = Color(0xFF007AFF)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF5F5F5)
            )
        )

        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Country List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Recently Used Section
            if (showRecentlyUsed && recentlyUsedCountries.isNotEmpty() && searchQuery.isEmpty()) {
                item {
                    Text(
                        text = "RECENTLY USED",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column {
                            recentlyUsedCountries.forEachIndexed { index, country ->
                                CountryItem(
                                    country = country,
                                    onClick = { onCountrySelected(country) },
                                    displayOptions = itemDisplayOptions
                                )
                                if (index < recentlyUsedCountries.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(start = 68.dp),
                                        color = Color(0xFFE5E5E5)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Alphabetically Grouped Countries
            groupedCountries.forEach { (letter, countriesInGroup) ->
                item {
                    Text(
                        text = letter.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column {
                            countriesInGroup.forEachIndexed { index, country ->
                                CountryItem(
                                    country = country,
                                    onClick = { onCountrySelected(country) },
                                    displayOptions = itemDisplayOptions
                                )
                                if (index < countriesInGroup.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(start = 68.dp),
                                        color = Color(0xFFE5E5E5)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = {
            Text(
                text = "Search",
                fontSize = 17.sp,
                color = Color(0xFF8E8E93)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF8E8E93)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
private fun CountryItem(
    country: Country,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    displayOptions: Set<CountryDisplayOption> = CountryDisplayDefaults.All
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountryDisplayText(
            country = country,
            displayOptions = displayOptions,
            modifier = Modifier.weight(1f)
        )
    }
}
