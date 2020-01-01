package com.vaudibert.canidrive.domain

import com.vaudibert.canidrive.R

object DriveLaws {
    // need to expose a list of handled country legislation
    val countryLaws = listOf(
        // Albania
        DriveLaw("AL", 0.1),

        // Austria
        DriveLaw("AT" , 0.5,
            YoungLimit(0.1, R.string.two_years_young_driver),
            professionalLimit = ProfessionalLimit(0.2)),


        // Belarus
        DriveLaw("BY", 0.3),

        // Belgium
        DriveLaw("BE" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Bosnia & Herzegovina
        DriveLaw("BA", 0.3,
            YoungLimit(explanationId = R.string.twenty_one_young_or_less_three_years_driving),
            ProfessionalLimit()),

        // Bulgaria
        DriveLaw("BG", 0.5),

        // Croatia
        DriveLaw("HR" , 0.5,
            YoungLimit(explanationId = R.string.sixteen_to_twenty_four_years_old),
            ProfessionalLimit()),

        // Cyprus
        DriveLaw("CY" , 0.5),

        // Czech republic
        DriveLaw("CZ"),

        //Denmark
        DriveLaw("DK", 0.5),

        // Estonia
        DriveLaw("EE" , 0.19),

        // Finland
        DriveLaw("FI" , 0.5),

        // France
        DriveLaw("FR" , 0.5,
            YoungLimit(0.2, R.string.two_years_young_driver),
            ProfessionalLimit(0.2)),

        // Georgia
        DriveLaw("GE", 0.2),

        // Germany
        DriveLaw("DE" , 0.5,
            YoungLimit(explanationId = R.string.twenty_one_young_or_less_two_years_driving),
            ProfessionalLimit(0.2)),

        // Gibraltar
        DriveLaw("GI", 0.5),

        // Greece
        DriveLaw("GR" , 0.5,
            YoungLimit(0.2, R.string.motor_cycle_or_less_two_years_driving),
            ProfessionalLimit(0.2)),

        // Hungary
        DriveLaw("HU"),

        // Iceland
        // TODO : check data
        DriveLaw("IS" , 0.2),

        // Ireland
        DriveLaw("IE" , 0.5,
            YoungLimit(0.2, R.string.motor_cycle_or_less_two_years_driving),
            ProfessionalLimit(0.2)),

        // Italy
        DriveLaw("IT" , 0.5,
            YoungLimit(0.01, R.string.three_years_driving),
            ProfessionalLimit(0.01)),

        // Latvia
        DriveLaw("LV", 0.5,
            YoungLimit(0.2, R.string.two_years_young_driver)
        ),

        // Liechtenstein
        // TODO : check if code is OK
        DriveLaw("FL", 0.8),

        // Lithuania
        DriveLaw("", 0.4,
            YoungLimit(explanationId = R.string.motor_cycle_or_less_two_years_driving),
            ProfessionalLimit()
        ),

        // Luxemburg
        DriveLaw("LU" , 0.5,
            YoungLimit(0.2, R.string.two_years_young_driver),
            ProfessionalLimit(0.2)),

        // Malta
        DriveLaw("MT", 0.8),

        // Moldova
        DriveLaw("MD", 0.3),

        // Montenegro
        DriveLaw("ME", 0.3),

        // Netherlands
        // TODO : check data, missing professional ?
        DriveLaw("NL" , 0.5,
            YoungLimit(0.2, R.string.five_years_driving)),

        // TODO : North macedonia

        // Norway
        DriveLaw("NO" , 0.2),

        // Poland
        DriveLaw("PL" , 0.2),

        // Portugal
        DriveLaw("PT" , 0.5,
            YoungLimit(0.2, R.string.three_years_driving)
        ),

        // Romania
        DriveLaw("RO"),

        // Russia
        DriveLaw("RU", 0.356),

        // Serbia





        // ----------------------------- Not up-to-date below ---------------------

        // Great-Britain (includes scotland...)
        // TODO : treat special case of scotland (not a country yet different law).
        DriveLaw("GB" , 0.8,
            professionalLimit = ProfessionalLimit(0.8)
        ),


        // Switzland
        DriveLaw("CH" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Spain
        DriveLaw("ES" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Slovenia
        DriveLaw("SI" , 0.5),

        // Sweden
        DriveLaw("SE" , 0.2, professionalLimit = ProfessionalLimit(0.2)),

        // Serbia
        DriveLaw("RS" , 0.2),

        // Slovakia
        DriveLaw("SK"),

        // Ukraine
        DriveLaw("UA")
    ).sortedBy { law -> law.countryCode }
    // need to produce a drivelaw regarding a country
}

data class DriveLaw(
    val countryCode:String,
    val limit:Double = 0.01,
    val youngLimit: YoungLimit? = null,
    val professionalLimit: ProfessionalLimit? = null
)

data class YoungLimit(val limit:Double = 0.01, val explanationId: Int)

data class ProfessionalLimit(val limit:Double = 0.01)