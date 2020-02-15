package com.vaudibert.canidrive.domain

import com.vaudibert.canidrive.domain.digestion.DigestionService
import com.vaudibert.canidrive.domain.drivelaw.DriveLawService
import java.util.*

class DrinkerStatusService(
    val digestionService: DigestionService,
    val driveLawService: DriveLawService
) {
    fun status() : DrinkerStatus {
        val driveLimit = driveLawService.driveLimit()
        return DrinkerStatus(
            digestionService.alcoholRateAt(Date()) <= driveLimit,
            digestionService.alcoholRateAt(Date()),
            digestionService.timeToReachLimit(driveLimit),
            digestionService.timeToReachLimit(driveLawService.defaultLimit)
        )
    }
}