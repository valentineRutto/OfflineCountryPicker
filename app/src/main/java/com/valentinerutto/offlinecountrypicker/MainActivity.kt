package com.valentinerutto.offlinecountrypicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.valentinerutto.offlinecountrypicker.ui.PhoneNumberInput
import com.valentinerutto.offlinecountrypicker.ui.theme.OfflineCountryPickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfflineCountryPickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    //AuthScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun AuthScreen() {

    var selectedCountry by remember { mutableStateOf(OfflineCountryPicker.getAllCountries().first()) }

    var phoneNumber by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        PhoneNumberInput(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = { phoneNumber = it },
            selectedCountry = selectedCountry,
            onCountrySelected = { selectedCountry = it },
            label = "Enter your phone number"
        )

        Button(
            onClick = {
                val fullNumber = "${selectedCountry.dialCode}$phoneNumber"
                // Use fullNumber for authentication
            }
        ) {
            Text("Continue")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OfflineCountryPickerTheme {
        Greeting("Android")
    }
}