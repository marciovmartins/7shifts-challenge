package com.github.marciovmartins.sevenshifts

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class LaborHour {
    var normal: Long = 0
        private set
    var overtime: Long = 0
        private set
    private val dailyDuration = HashMap<LocalDate, Long>()

    private val maximumDailyOvertime = TimeUnit.HOURS.toMinutes(8) // TODO: extract to config
    private val maximumWeeklyOvertime = TimeUnit.HOURS.toMinutes(40) // TODO: extract to config

    fun add(startDateTime: LocalDateTime, endDateTime: LocalDateTime) { // TODO: create ValueObject Punch
        val duration = Duration.between(startDateTime, endDateTime).toMinutes()
        val dailyDuration = this.getDailyDurationOf(startDateTime)

        if (dailyDuration + duration > this.maximumDailyOvertime) {
            if (dailyDuration < this.maximumDailyOvertime) {
                this.normal += this.maximumDailyOvertime - dailyDuration
            }
            this.overtime += (dailyDuration + duration) - this.maximumDailyOvertime
        } else {
            this.normal += duration
        }

        this.incrementDailyHoursOf(startDateTime, duration)
        this.adjustWeeklyOvertime()
    }

    private fun getDailyDurationOf(dateTime: LocalDateTime): Long = if (this.dailyDuration.containsKey(dateTime.toLocalDate())) {
        this.dailyDuration[dateTime.toLocalDate()]!!
    } else {
        0L
    }

    private fun incrementDailyHoursOf(dateTime: LocalDateTime, duration: Long) {
        this.dailyDuration[dateTime.toLocalDate()] = this.getDailyDurationOf(dateTime) + duration
    }

    private fun adjustWeeklyOvertime() {
        if (this.normal > this.maximumWeeklyOvertime) {
            this.overtime += this.normal - this.maximumWeeklyOvertime
            this.normal = this.maximumWeeklyOvertime
        }
    }
}
