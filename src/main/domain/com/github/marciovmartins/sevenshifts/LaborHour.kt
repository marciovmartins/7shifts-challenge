package com.github.marciovmartins.sevenshifts

import java.time.Duration
import java.time.LocalDateTime

class LaborHour {
    var normal: Long = 0
        private set
    var overtime: Long = 0
        private set

    fun add(startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        this.normal += Duration.between(startDateTime, endDateTime).toMinutes()
    }
}
