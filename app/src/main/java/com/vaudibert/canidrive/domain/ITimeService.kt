package com.vaudibert.canidrive.domain

interface ITimeService {
    fun nowInMillis(): Long
    fun isSaintPatrick(): Boolean
}