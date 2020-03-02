package com.vaudibert.canidrive.domain

interface TimeService {
    fun nowInMillis(): Long
    fun isSaintPatrick(): Boolean
}