package com.valentinerutto.offlinecountrypicker.data.model

object CountryDataProvider
{

     val countries = listOf(
        Country("US", "United States", "+1", "🇺🇸", "USD", "North America", "Washington D.C.", listOf("English")),
        Country("GB", "United Kingdom", "+44", "🇬🇧", "GBP", "Europe", "London", listOf("English")),
        Country("CA", "Canada", "+1", "🇨🇦", "CAD", "North America", "Ottawa", listOf("English", "French")),
        Country("AU", "Australia", "+61", "🇦🇺", "AUD", "Oceania", "Canberra", listOf("English")),
        Country("DE", "Germany", "+49", "🇩🇪", "EUR", "Europe", "Berlin", listOf("German")),
        Country("FR", "France", "+33", "🇫🇷", "EUR", "Europe", "Paris", listOf("French")),
        Country("IN", "India", "+91", "🇮🇳", "INR", "Asia", "New Delhi", listOf("Hindi", "English")),
        Country("CN", "China", "+86", "🇨🇳", "CNY", "Asia", "Beijing", listOf("Mandarin")),
        Country("JP", "Japan", "+81", "🇯🇵", "JPY", "Asia", "Tokyo", listOf("Japanese")),
        Country("BR", "Brazil", "+55", "🇧🇷", "BRL", "South America", "Brasília", listOf("Portuguese")),
        Country("MX", "Mexico", "+52", "🇲🇽", "MXN", "North America", "Mexico City", listOf("Spanish")),
        Country("ZA", "South Africa", "+27", "🇿🇦", "ZAR", "Africa", "Pretoria", listOf("English", "Afrikaans")),
        Country("NG", "Nigeria", "+234", "🇳🇬", "NGN", "Africa", "Abuja", listOf("English")),
        Country("KE", "Kenya", "+254", "🇰🇪", "KES", "Africa", "Nairobi", listOf("English", "Swahili")),
        Country("EG", "Egypt", "+20", "🇪🇬", "EGP", "Africa", "Cairo", listOf("Arabic")),
        Country("IT", "Italy", "+39", "🇮🇹", "EUR", "Europe", "Rome", listOf("Italian")),
        Country("ES", "Spain", "+34", "🇪🇸", "EUR", "Europe", "Madrid", listOf("Spanish")),
        Country("NL", "Netherlands", "+31", "🇳🇱", "EUR", "Europe", "Amsterdam", listOf("Dutch")),
        Country("SE", "Sweden", "+46", "🇸🇪", "SEK", "Europe", "Stockholm", listOf("Swedish")),
        Country("CH", "Switzerland", "+41", "🇨🇭", "CHF", "Europe", "Bern", listOf("German", "French", "Italian")),
        Country("SG", "Singapore", "+65", "🇸🇬", "SGD", "Asia", "Singapore", listOf("English", "Mandarin", "Malay")),
        Country("AE", "United Arab Emirates", "+971", "🇦🇪", "AED", "Asia", "Abu Dhabi", listOf("Arabic")),
        Country("SA", "Saudi Arabia", "+966", "🇸🇦", "SAR", "Asia", "Riyadh", listOf("Arabic")),
        Country("RU", "Russia", "+7", "🇷🇺", "RUB", "Europe", "Moscow", listOf("Russian")),
        Country("KR", "South Korea", "+82", "🇰🇷", "KRW", "Asia", "Seoul", listOf("Korean")),
        Country("PH", "Philippines", "+63", "🇵🇭", "PHP", "Asia", "Manila", listOf("Filipino", "English")),
        Country("ID", "Indonesia", "+62", "🇮🇩", "IDR", "Asia", "Jakarta", listOf("Indonesian")),
        Country("TH", "Thailand", "+66", "🇹🇭", "THB", "Asia", "Bangkok", listOf("Thai")),
        Country("MY", "Malaysia", "+60", "🇲🇾", "MYR", "Asia", "Kuala Lumpur", listOf("Malay")),
        Country("VN", "Vietnam", "+84", "🇻🇳", "VND", "Asia", "Hanoi", listOf("Vietnamese")),
        Country("PK", "Pakistan", "+92", "🇵🇰", "PKR", "Asia", "Islamabad", listOf("Urdu", "English")),
        Country("BD", "Bangladesh", "+880", "🇧🇩", "BDT", "Asia", "Dhaka", listOf("Bengali")),
        Country("TR", "Turkey", "+90", "🇹🇷", "TRY", "Asia", "Ankara", listOf("Turkish")),
        Country("AR", "Argentina", "+54", "🇦🇷", "ARS", "South America", "Buenos Aires", listOf("Spanish")),
        Country("CL", "Chile", "+56", "🇨🇱", "CLP", "South America", "Santiago", listOf("Spanish")),
        Country("CO", "Colombia", "+57", "🇨🇴", "COP", "South America", "Bogotá", listOf("Spanish")),
        Country("PE", "Peru", "+51", "🇵🇪", "PEN", "South America", "Lima", listOf("Spanish")),
        Country("NZ", "New Zealand", "+64", "🇳🇿", "NZD", "Oceania", "Wellington", listOf("English")),
        Country("GH", "Ghana", "+233", "🇬🇭", "GHS", "Africa", "Accra", listOf("English")),
        Country("UG", "Uganda", "+256", "🇺🇬", "UGX", "Africa", "Kampala", listOf("English", "Swahili"))
    )


    fun getAllCountries(): List<Country> = countries

    fun getCountryByCode(code: String): Country? =
        countries.find { it.code.equals(code, ignoreCase = true) }

    fun getCountryByDialCode(dialCode: String): Country? =
        countries.find { it.dialCode == dialCode }

    fun searchCountries(query: String): List<Country> {
        val lowerQuery = query.lowercase()
        return countries.filter {
            it.name.lowercase().contains(lowerQuery) ||
                    it.code.lowercase().contains(lowerQuery) ||
                    it.dialCode.contains(lowerQuery)
        }
    }

    fun getCountriesByContinent(continent: String): List<Country> =
        countries.filter { it.continent?.equals(continent, ignoreCase = true) == true }
}