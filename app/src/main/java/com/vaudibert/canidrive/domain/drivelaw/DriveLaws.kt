package com.vaudibert.canidrive.domain.drivelaw

import com.vaudibert.canidrive.R

object DriveLaws {

    val default = DriveLaw("", 0.0)

    // Main source : https://en.wikipedia.org/wiki/Drunk_driving_law_by_country
    val list = listOf(

    // Other
        default,

    // -------------------------- Europe --------------------------
    // Albania
        DriveLaw("AL", 0.1),

    // Austria
        DriveLaw(
            "AT", 0.5,
            YoungLimit(
                0.1,
                R.string.less_two_years_driving
            ),
            professionalLimit = ProfessionalLimit(
                0.2
            )
        ),

    // Belarus
        DriveLaw("BY", 0.3),

    // Belgium
        DriveLaw(
            "BE",
            0.5,
            professionalLimit = ProfessionalLimit(
                0.2
            )
        ),

    // Bosnia & Herzegovina
        DriveLaw(
            "BA", 0.3,
            YoungLimit(explanationId = R.string.twenty_one_young_or_less_three_years_driving),
            ProfessionalLimit()
        ),

    // Bulgaria
        DriveLaw("BG", 0.5),

    // Croatia
        DriveLaw(
            "HR", 0.5,
            YoungLimit(explanationId = R.string.sixteen_to_twenty_four_years_old),
            ProfessionalLimit()
        ),

    // Cyprus
        DriveLaw("CY", 0.5),

    // Czech republic
        DriveLaw("CZ"),

    //Denmark
        DriveLaw("DK", 0.5),

    // Estonia
        DriveLaw("EE", 0.19),

    // Finland
        DriveLaw("FI", 0.5),

    // France
        DriveLaw(
            "FR", 0.5,
            YoungLimit(
                0.2,
                R.string.less_two_years_driving
            ),
            ProfessionalLimit(0.2)
        ),

    // Georgia
        DriveLaw("GE", 0.2),

    // Germany
        DriveLaw(
            "DE", 0.5,
            YoungLimit(explanationId = R.string.twenty_one_young_or_less_two_years_driving),
            ProfessionalLimit(0.2)
        ),

    // Gibraltar
        DriveLaw("GI", 0.5),

    // Greece
        DriveLaw(
            "GR", 0.5,
            YoungLimit(
                0.2,
                R.string.motor_cycle_or_less_two_years_driving
            ),
            ProfessionalLimit(0.2)
        ),

    // Hungary
        DriveLaw("HU"),

    // Iceland
    // TODO : check data
        DriveLaw("IS", 0.2),

    // Ireland
        DriveLaw(
            "IE", 0.5,
            YoungLimit(
                0.2,
                R.string.motor_cycle_or_less_two_years_driving
            ),
            ProfessionalLimit(0.2)
        ),

    // Italy
        DriveLaw(
            "IT", 0.5,
            YoungLimit(
                0.01,
                R.string.three_years_driving
            ),
            ProfessionalLimit(0.01)
        ),

    // Latvia
        DriveLaw(
            "LV", 0.5,
            YoungLimit(
                0.2,
                R.string.less_two_years_driving
            )
        ),

    // Lithuania
        DriveLaw(
            "LT", 0.4,
            YoungLimit(explanationId = R.string.motor_cycle_or_less_two_years_driving),
            ProfessionalLimit()
        ),

    // Luxemburg
        DriveLaw(
            "LU", 0.5,
            YoungLimit(
                0.2,
                R.string.less_two_years_driving
            ),
            ProfessionalLimit(0.2)
        ),

    // Malta
        DriveLaw("MT", 0.8),

    // Moldova
        DriveLaw("MD", 0.3),

    // Montenegro
        DriveLaw("ME", 0.3),

    // Netherlands
    // TODO : check data, missing professional ?
        DriveLaw(
            "NL", 0.5,
            YoungLimit(
                0.2,
                R.string.five_years_driving
            )
        ),

    // TODO : North macedonia

    // Norway
        DriveLaw("NO", 0.2),

    // Poland
        DriveLaw("PL", 0.2),

    // Portugal
        DriveLaw(
            "PT", 0.5,
            YoungLimit(
                0.2,
                R.string.three_years_driving
            )
        ),

    // Romania
        DriveLaw("RO"),

    // Russia
        DriveLaw("RU", 0.356),

    // Serbia
        DriveLaw(
            "RS", 0.2,
            YoungLimit(explanationId = R.string.motor_cycle_or_young_drivers),
            ProfessionalLimit()
        ),

    // Slovakia
        DriveLaw("SK"),

    // Slovenia
        DriveLaw(
            "SI", 0.5,
            YoungLimit(explanationId = R.string.three_years_driving),
            ProfessionalLimit()
        ),

    // Spain
        DriveLaw(
            "ES", 0.5,
            YoungLimit(
                0.3,
                R.string.less_two_years_driving
            ),
            ProfessionalLimit(0.2)
        ),

    // Sweden
        DriveLaw("SE", 0.2),

    // Switzland
        DriveLaw(
            "CH", 0.5,
            YoungLimit(explanationId = R.string.three_years_driving)
        ),

    // Ukraine
        DriveLaw("UA", 0.2),

    // Great-Britain (includes scotland...)
    // TODO : treat special case of scotland (not a country yet different law).
        DriveLaw(
            "GB", 0.8,
            professionalLimit = ProfessionalLimit(
                0.8
            )
        ),

    // -------------------- Americas --------------------
    // Canada
        DriveLaw("CA", 0.8),

    // USA
        DriveLaw(
            "US", 0.8,
            YoungLimit(explanationId = R.string.twenty_one_young)
        ),

    // -------------------- Asia --------------------
    // China
        DriveLaw("CN"),

    // Hong-Kong
        DriveLaw("HK", 0.5),

    // Japan
        DriveLaw("JP", 0.3),

    // South Korea
        DriveLaw("KR", 0.3),

    // Taiwan
        DriveLaw("TW", 0.3),

    // India
        DriveLaw("IN", 0.3),

    // Nepal
        DriveLaw("NP"),

    // Pakistan
        DriveLaw("PK"),

    // Sri-lanka
        DriveLaw("LK", 0.6),

    // Indonesia
        DriveLaw("ID"),

    // Laos
        DriveLaw("LA", 0.8),

    // Malaysia
        DriveLaw("MY", 0.8),

    // Philippines
        DriveLaw(
            "PH", 0.5,
            YoungLimit(
                0.1,
                R.string.motor_cycle_or_young_drivers
            ),
            ProfessionalLimit(0.1)
        ),

    // Singapore
        DriveLaw("SG", 0.8),

    // Thailand
        DriveLaw(
            "TH",
            0.5,
            professionalLimit = ProfessionalLimit()
        ),

    // Vietnam
    // TODO : weird case for motorbikes : 0.5
        DriveLaw("VN"),

    // Armenia
        DriveLaw("AM", 0.4),

    // Iran
        DriveLaw("IR"),

    // Israel
        DriveLaw(
            "IL", 0.24,
            YoungLimit(
                0.05,
                R.string.new_driver_or_twenty_four
            ),
            ProfessionalLimit(0.05)
        ),

    // Jordan
        DriveLaw("JO", 0.5),

    // Kuwait
        DriveLaw("KW"),

    // Saudi Arabia
        DriveLaw("SA"),

    // United Arab Emirates
        DriveLaw("AE"),

    // Turkey
        DriveLaw(
            "TR",
            0.5,
            professionalLimit = ProfessionalLimit()
        )

    )
}