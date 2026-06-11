package com.valentinerutto.offlinecountrypicker.data

import com.valentinerutto.offlinecountrypicker.data.model.Country
import com.valentinerutto.offlinecountrypicker.data.model.CountryDataProvider
import com.valentinerutto.offlinecountrypicker.data.model.PhoneNumber

class CountryRepository {

    fun getAllCountries():List<Country> = CountryDataProvider.getAllCountries()

}