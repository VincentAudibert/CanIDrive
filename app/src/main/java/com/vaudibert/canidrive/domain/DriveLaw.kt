package com.vaudibert.canidrive.domain

import com.vaudibert.canidrive.R

object DriveLaws {
    // need to expose a list of handled country legislation
    val countryLaws = listOf(
        // France
        DriveLaw("FR" , 0.5,
            YoungLimit(0.2, R.string.two_years_young_driver),
            ProfessionalLimit(0.2)),

        // Great-Britain (includes scotland...)
        // TODO : treat special case of scotland (not a country yet different law).
        DriveLaw("GB" , 0.8,
            professionalLimit = ProfessionalLimit(0.8)
        ),

        // Belgium
        DriveLaw("BE" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Netherlands
        DriveLaw("NL" , 0.5),

        // Deutshland
        DriveLaw("DE" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Luxemburg
        DriveLaw("LU" , 0.5),

        // Switzland
        DriveLaw("CH" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Italy
        DriveLaw("IT" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Finland
        DriveLaw("FI" , 0.5),

        // Spain
        DriveLaw("ES" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Portugal
        DriveLaw("PT" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Greece
        DriveLaw("GR" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Austria
        DriveLaw("AT" , 0.5,
            YoungLimit(0.1, R.string.two_years_young_driver),
            professionalLimit = ProfessionalLimit(0.2)),

        // ???
        DriveLaw("SI" , 0.5),

        // ??
        DriveLaw("HR" , 0.5),

        // Ireland
        DriveLaw("IE" , 0.5, professionalLimit = ProfessionalLimit(0.2)),

        // Iceland
        DriveLaw("IS" , 0.5),

        // Norway
        DriveLaw("NO" , 0.2, professionalLimit = ProfessionalLimit(0.2)),

        // Sweden
        DriveLaw("SE" , 0.2, professionalLimit = ProfessionalLimit(0.2)),

        // Estonia ?
        DriveLaw("EE" , 0.2),

        // ??
        DriveLaw("RS" , 0.2),

        // Poland
        DriveLaw("PL" , 0.2, professionalLimit = ProfessionalLimit(0.2)),

        // Bosnia & Herzegovina
        DriveLaw("BA", 0.3,
            YoungLimit(explanationId = R.string.twenty_one_young_or_less_three_years_driving),
            ProfessionalLimit()),


        DriveLaw("CZ"),

        // Slovakia
        DriveLaw("SK"),

        // ??
        DriveLaw("HU"),

        // Romania
        DriveLaw("RO"),

        // ??
        DriveLaw("UA"),

        // ??
        DriveLaw("AM")

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