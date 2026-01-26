package com.valentinerutto.offlinecountrypicker.data

import com.valentinerutto.offlinecountrypicker.data.model.Country
import com.valentinerutto.offlinecountrypicker.data.model.CountryDataProvider
import com.valentinerutto.offlinecountrypicker.data.model.PhoneNumber

class CountryRepository {

    fun getAllCountries():List<Country> = CountryDataProvider.getAllCountries()
    fun getCountryByCode(code:String):Country? = CountryDataProvider.getCountryByCode(code)
    fun getCountryByDialCode(dialCode:String):Country? = CountryDataProvider.getCountryByDialCode(dialCode)
    fun searchCountries(query:String):List<Country> = CountryDataProvider.searchCountries(query)
    fun getCountriesByContinent(continent:String):List<Country> = CountryDataProvider.getCountriesByContinent(continent)
    fun detectCountryFromLocale(locale:String) = CountryDataProvider.getCountryByCode(locale)

    fun parsePhoneNumber(number:String): PhoneNumber?{

        if(!number.startsWith("+")) return null

        val countries = getAllCountries()

        for(country in countries){

            if(number.startsWith(country.dialCode)){
                val formattedNumber = number.removePrefix(country.dialCode)
                return PhoneNumber(country, formattedNumber, number)
            }
        }
        return null
    }

}