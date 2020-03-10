package com.vaudibert.canidrive.domain

import com.vaudibert.canidrive.domain.digestion.DigestionService
import com.vaudibert.canidrive.domain.drivelaw.DriveLawService
import java.util.*

class DrinkerStatusService(
    private val digestionService: DigestionService,
    private val driveLawService: DriveLawService
) {
    fun status() : DrinkerStatus {
        val driveLimit = driveLawService.driveLimit()
        val ratePresent = digestionService.alcoholRateAt(Date())
        return DrinkerStatus(
            ratePresent <= driveLimit,
            ratePresent,
            digestionService.timeToReachLimit(driveLimit),
            digestionService.timeToReachLimit(driveLawService.defaultLimit)
        )
    }
}