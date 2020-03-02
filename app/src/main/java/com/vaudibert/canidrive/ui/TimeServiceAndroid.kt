package com.vaudibert.canidrive.ui

import com.vaudibert.canidrive.domain.TimeService
import java.util.*

class TimeServiceAndroid : TimeService {
    override fun nowInMillis() = Date().time

    override fun isSaintPatrick(): Boolean {
        val now = Date()
        return now.month == 3 && (now.day in 16..18)
    }
}