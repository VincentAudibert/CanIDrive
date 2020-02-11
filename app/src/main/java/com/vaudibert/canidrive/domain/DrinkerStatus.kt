package com.vaudibert.canidrive.domain

import java.util.*

data class DrinkerStatus(
    val canDrive : Boolean,
    val alcoholRate: Double,
    val canDriveDate : Date,
    val soberDate : Date
)