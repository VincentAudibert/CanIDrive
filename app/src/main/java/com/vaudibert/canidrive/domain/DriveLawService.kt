package com.vaudibert.canidrive.domain

import java.util.*
import kotlin.math.min

class DriveLawService {

    val defaultLimit = 0.0

    var onCustomLimitCallback = { _:Double -> }

    var customCountryLimit = 0.0
        set(value) {
            field = value
            onCustomLimitCallback(value)
        }

    var onYoungCallback = { _:Boolean -> }

    var isYoung: Boolean = false
        set(value) {
            field = value
            onYoungCallback(value)
        }

    var onProfessionalCallback = { _:Boolean -> }

    var isProfessional: Boolean = false
        set(value) {
            field = value
            onProfessionalCallback(value)
        }

    private val countryLaws = DriveLaws.list
        .sortedBy { law -> Locale("", law.countryCode).displayCountry }

    var driveLaw = DriveLaws.default

    fun getListOfCountriesWithFlags(other: String): List<String> {
        return countryLaws.map { law ->
            if (law.countryCode == "")
                other
            else
                stringToFlagEmoji(law.countryCode) + " " + Locale("", law.countryCode).displayCountry
        }
    }

    private fun getIndexOf(countryCode: String?): Int {
        if (countryCode == null) return 0

        val index = countryLaws
            .indexOfFirst {
                    law -> law.countryCode == countryCode
            }
        return index.coerceAtLeast(0)
    }

    fun getIndexOfCurrent() = getIndexOf(driveLaw.countryCode)

    private fun findByCountryCode(countryCode: String): DriveLaw {
        return countryLaws.find {
                law -> law.countryCode == countryCode
        } ?: DriveLaws.default
    }

    var onSelectCallback = { _:String -> }

    fun select(countryCode: String) {
        driveLaw = findByCountryCode(countryCode)
        onSelectCallback(countryCode)
    }

    fun select(position: Int) {
        driveLaw = countryLaws[position]
        onSelectCallback(driveLaw.countryCode)
    }

    fun driveLimit() : Double {
        if (driveLaw == DriveLaws.default) return customCountryLimit

        val regularLimit = driveLaw.limit

        val youngLimit = if (isYoung)
            driveLaw.youngLimit?.limit ?: regularLimit
        else
            regularLimit

        val professionalLimit = if (isProfessional)
            driveLaw.professionalLimit?.limit ?: regularLimit
        else
            regularLimit

        return min(youngLimit, professionalLimit)
    }

    /**
     * This method is to change the country code like "us" into 🇺🇸
     * Stolen from https://stackoverflow.com/a/35849652/75579
     * 1. It first checks if the string consists of only 2 characters: ISO 3166-1 alpha-2 two-letter country codes (https://en.wikipedia.org/wiki/Regional_Indicator_Symbol).
     * 2. It then checks if both characters are alphabet
     * do nothing if it doesn't fulfil the 2 checks
     * caveat: if you enter an invalid 2 letter country code, say "XX", it will pass the 2 checks, and it will return unknown result
     */
    private fun stringToFlagEmoji(twoCharString: String): String {
        // 1. It first checks if the string consists of only 2 characters: ISO 3166-1 alpha-2 two-letter country codes (https://en.wikipedia.org/wiki/Regional_Indicator_Symbol).
        if (twoCharString.length != 2) {
            return twoCharString
        }

        val countryCodeCaps = twoCharString.toUpperCase(Locale.getDefault()) // upper case is important because we are calculating offset
        val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
        val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

        // 2. It then checks if both characters are alphabet
        if (!countryCodeCaps[0].isLetter() || !countryCodeCaps[1].isLetter()) {
            return twoCharString
        }

        return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
    }
}