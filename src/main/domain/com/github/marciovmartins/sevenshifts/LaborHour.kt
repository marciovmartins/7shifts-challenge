package com.github.marciovmartins.sevenshifts

import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class LaborHour {
    var normal: Long = 0
        private set
    var overtime: Long = 0
        private set

    private val maximumDailyOvertime = TimeUnit.HOURS.toMinutes(8)
    private val maximumWeeklyOvertime = TimeUnit.HOURS.toMinutes(40)

    fun add(startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        val duration = Duration.between(startDateTime, endDateTime).toMinutes()

        if (duration > this.maximumDailyOvertime) {
            this.normal += maximumDailyOvertime
            this.overtime += duration - maximumDailyOvertime
        } else {
            this.normal += duration
        }

        this.adjustWeeklyOvertime()
    }

    private fun adjustWeeklyOvertime() {
        if (this.normal > this.maximumWeeklyOvertime) {
            this.overtime += this.normal - this.maximumWeeklyOvertime
            this.normal = this.maximumWeeklyOvertime
        }
    }
}
