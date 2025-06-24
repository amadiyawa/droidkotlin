package com.amadiyawa.feature_base.common.util

import com.droidkotlin.core.R
import com.amadiyawa.feature_base.domain.model.Country

object CountryUtil {
    // Convert country code to emoji flag
    fun getFlagEmojiForCountryCode(countryCode: String): String {
        val firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6
        val secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6

        return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
    }

    // Get list of countries with emoji flags
    fun getCountries(): List<Country> = listOf(
        Country(
            code = "JP",
            nameResId = R.string.country_jp,
            dialCode = "+81",
            flagEmoji = getFlagEmojiForCountryCode("JP"),
            phoneExample = "90 1234 5678"
        ),
        Country(
            code = "CM",
            nameResId = R.string.country_cm,
            dialCode = "+237",
            flagEmoji = getFlagEmojiForCountryCode("CM"),
            phoneExample = "6 75 12 34 56"
        ),
        Country(
            code = "NG",
            nameResId = R.string.country_ng,
            dialCode = "+234",
            flagEmoji = getFlagEmojiForCountryCode("NG"),
            phoneExample = "802 123 4567"
        ),
        Country(
            code = "US",
            nameResId = R.string.country_us,
            dialCode = "+1",
            flagEmoji = getFlagEmojiForCountryCode("US"),
            phoneExample = "(201) 555-0123"
        ),
        Country(
            code = "CA",
            nameResId = R.string.country_ca,
            dialCode = "+1",
            flagEmoji = getFlagEmojiForCountryCode("CA"),
            phoneExample = "(604) 555-0123"
        ),
        Country(
            code = "GB",
            nameResId = R.string.country_gb,
            dialCode = "+44",
            flagEmoji = getFlagEmojiForCountryCode("GB"),
            phoneExample = "7700 900123"
        ),
        Country(
            code = "FR",
            nameResId = R.string.country_fr,
            dialCode = "+33",
            flagEmoji = getFlagEmojiForCountryCode("FR"),
            phoneExample = "6 12 34 56 78"
        ),
        Country(
            code = "DE",
            nameResId = R.string.country_de,
            dialCode = "+49",
            flagEmoji = getFlagEmojiForCountryCode("DE"),
            phoneExample = "151 2345678"
        ),
        Country(
            code = "IT",
            nameResId = R.string.country_it,
            dialCode = "+39",
            flagEmoji = getFlagEmojiForCountryCode("IT"),
            phoneExample = "312 345 6789"
        ),
        Country(
            code = "ES",
            nameResId = R.string.country_es,
            dialCode = "+34",
            flagEmoji = getFlagEmojiForCountryCode("ES"),
            phoneExample = "612 34 56 78"
        ),
        Country(
            code = "AU",
            nameResId = R.string.country_au,
            dialCode = "+61",
            flagEmoji = getFlagEmojiForCountryCode("AU"),
            phoneExample = "412 345 678"
        ),
        Country(
            code = "RW",
            nameResId = R.string.country_rw,
            dialCode = "+250",
            flagEmoji = getFlagEmojiForCountryCode("RW"),
            phoneExample = "72 123 4567"
        ),
        Country(
            code = "CN",
            nameResId = R.string.country_cn,
            dialCode = "+86",
            flagEmoji = getFlagEmojiForCountryCode("CN"),
            phoneExample = "131 2345 6789"
        ),
        Country(
            code = "IN",
            nameResId = R.string.country_in,
            dialCode = "+91",
            flagEmoji = getFlagEmojiForCountryCode("IN"),
            phoneExample = "98765 12345"
        ),
        Country(
            code = "BR",
            nameResId = R.string.country_br,
            dialCode = "+55",
            flagEmoji = getFlagEmojiForCountryCode("BR"),
            phoneExample = "11 91234 5678"
        ),
        Country(
            code = "RU",
            nameResId = R.string.country_ru,
            dialCode = "+7",
            flagEmoji = getFlagEmojiForCountryCode("RU"),
            phoneExample = "912 345-67-89"
        ),
        Country(
            code = "MX",
            nameResId = R.string.country_mx,
            dialCode = "+52",
            flagEmoji = getFlagEmojiForCountryCode("MX"),
            phoneExample = "1 234 567 8900"
        ),
        Country(
            code = "ZA",
            nameResId = R.string.country_za,
            dialCode = "+27",
            flagEmoji = getFlagEmojiForCountryCode("ZA"),
            phoneExample = "71 123 4567"
        )
    )

    // Find country by dial code
    fun findCountryByDialCode(dialCode: String): Country? {
        return getCountries().find { it.dialCode == dialCode }
    }
}