package com.github.marciovmartins.sevenshifts

import java.time.LocalDate

class LabourHour(private val user: User) {
    var normal: Long = 0
        private set
    var overtime: Long = 0
        private set
    private val dailyDuration = HashMap<LocalDate, Long>()

    private val maximumDailyOvertime = this.user.location.labourSettings.dailyOvertimeThreshold
    private val maximumWeeklyOvertime = this.user.location.labourSettings.weeklyOvertimeThreshold

    fun add(punch: Punch) {
        val dailyDuration = this.getDailyDurationOf(punch)

        if (dailyDuration + punch.duration > this.maximumDailyOvertime) {
            if (dailyDuration < this.maximumDailyOvertime) {
                this.normal += this.maximumDailyOvertime - dailyDuration
                this.overtime += (dailyDuration + punch.duration) - this.maximumDailyOvertime
            } else {
                this.overtime += punch.duration
            }
        } else {
            this.normal += punch.duration
        }

        this.incrementDailyHoursOf(punch)
        this.adjustWeeklyOvertime()
    }

    private fun getDailyDurationOf(punch: Punch): Long = if (this.dailyDuration.containsKey(punch.day)) {
        this.dailyDuration[punch.day]!!
    } else {
        0L
    }

    private fun incrementDailyHoursOf(punch: Punch) {
        this.dailyDuration[punch.day] = this.getDailyDurationOf(punch) + punch.duration
    }

    private fun adjustWeeklyOvertime() {
        if (this.normal > this.maximumWeeklyOvertime) {
            this.overtime += this.normal - this.maximumWeeklyOvertime
            this.normal = this.maximumWeeklyOvertime
        }
    }
}
