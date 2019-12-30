package com.vaudibert.canidrive.domain

object DriveLaws {
    // need to expose a list of handled country legislation
    val countryLaws = listOf(
        DriveLaw("FR" , 0.5),
        DriveLaw("UK" , 0.8),
        DriveLaw("BE" , 0.5),
        DriveLaw("NL" , 0.5),
        DriveLaw("DE" , 0.5),
        DriveLaw("LU" , 0.5),
        DriveLaw("CH" , 0.5),
        DriveLaw("IT" , 0.5),
        DriveLaw("DH" , 0.5),
        DriveLaw("FI" , 0.5),
        DriveLaw("ES" , 0.5),
        DriveLaw("PT" , 0.5),
        DriveLaw("GR" , 0.5),
        DriveLaw("AT" , 0.5),
        DriveLaw("SI" , 0.5),
        DriveLaw("HR" , 0.5),
        DriveLaw("IE" , 0.5),
        DriveLaw("IS" , 0.5),
        DriveLaw("NO" , 0.2),
        DriveLaw("SE" , 0.2),
        DriveLaw("EE" , 0.2),
        DriveLaw("RS" , 0.2),
        DriveLaw("PL" , 0.2),
        DriveLaw("CZ"),
        DriveLaw("SK"),
        DriveLaw("HU"),
        DriveLaw("RO"),
        DriveLaw("UA"),
        DriveLaw("AM")
    )
    // need to produce a drivelaw regarding a country
}

data class DriveLaw(val countryCode:String, val limit:Double = 0.01)