package com.github.marciovmartins.sevenshifts

import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class LaborHour {
    var normal: Long = 0
        private set
    var overtime: Long = 0
        private set

    private val maximumRegularHours = TimeUnit.HOURS.toMinutes(8)

    fun add(startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        val duration = Duration.between(startDateTime, endDateTime).toMinutes()

        if (duration > maximumRegularHours) {
            this.normal += maximumRegularHours
            this.overtime += duration - maximumRegularHours
        } else {
            this.normal += duration
        }
    }
}
