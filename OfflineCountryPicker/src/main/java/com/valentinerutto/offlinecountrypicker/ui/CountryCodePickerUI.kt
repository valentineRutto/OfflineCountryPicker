package com.valentinerutto.offlinecountrypicker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.valentinerutto.offlinecountrypicker.data.CountryRepository
import com.valentinerutto.offlinecountrypicker.data.model.Country
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinerutto.offlinecountrypickerLibray.R

@Composable
fun CountryCodePickerUI (
    selectedCountry: Country?,
    onCountrySelected:(Country) -> Unit,
    modifier: Modifier = Modifier,
    repository: CountryRepository = remember { CountryRepository() }




){
    var showDialog by remember { mutableStateOf(false) }
    val displayCountry = selectedCountry ?: repository.getCountryByCode("KE")

    OutlinedButton(onClick = { showDialog = true }, modifier = modifier, shape = RoundedCornerShape(8.dp)) {
     displayCountry?.let {
     Text(text = it.flag , fontSize = 24.sp, modifier = Modifier.padding(end = 4.dp))
        Text(text = it.dialCode )

        Icon( imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select Country")



    }
    }



}