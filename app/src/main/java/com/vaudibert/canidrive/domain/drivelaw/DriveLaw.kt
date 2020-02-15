package com.vaudibert.canidrive.domain.drivelaw


data class DriveLaw(
    val countryCode:String,
    val limit:Double = 0.0,
    val youngLimit: YoungLimit? = null,
    val professionalLimit: ProfessionalLimit? = null
) {
    fun isCustom() = countryCode.isEmpty()
}

data class YoungLimit(val limit:Double = 0.0, val explanationId: Int)

data class ProfessionalLimit(val limit:Double = 0.0)

