package com.valentinerutto.offlinecountrypicker.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val code: String,           // ISO 3166-1 alpha-2 code (US, GB, etc)
    val name: String,           // Full country name
    val dialCode: String,       // Phone dial code (+1, +44, etc)
    val flag: String,           // Unicode emoji flag
    val currency: String?,      // Currency code (USD, GBP, etc)
    val continent: String?,     // Continent name
    val capital: String?,       // Capital city
    val languages: List<String>? // Languages spoken
) : Parcelable {
    val displayName: String
        get() = "$flag $name"

    val displayDialCode: String
        get() = "$flag $dialCode"
val displayflag:String
    get() = "$code $name"
}

data class PhoneNumber(
    val country: Country,
    val number: String,
    val formattedNumber: String
) {
    val fullNumber: String
        get() = "${country.dialCode}$number"

    val isValid: Boolean
        get() = number.length in 7..15 && number.all { it.isDigit() }
}
