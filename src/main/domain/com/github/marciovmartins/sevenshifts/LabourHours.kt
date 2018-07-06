package com.github.marciovmartins.sevenshifts

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class LabourHours(private val labourSettings: LabourSettings) {
    private val workedTimes = HashMap<LocalDate, WorkedTime>()
    private val dailyDuration = HashMap<LocalDate, Long>()
    private val maximumDailyOvertime = this.labourSettings.dailyOvertimeThreshold
    private val maximumWeeklyOvertime = this.labourSettings.weeklyOvertimeThreshold

    fun add(punch: Punch) {
        val workedTime = this.getFrom(punch.day.atStartOfDay())
        val dailyDuration = this.getDailyDurationOf(punch)

        if (dailyDuration + punch.duration > this.maximumDailyOvertime) {
            if (dailyDuration < this.maximumDailyOvertime) {
                workedTime.normal += this.maximumDailyOvertime - dailyDuration
                workedTime.overtime += (dailyDuration + punch.duration) - this.maximumDailyOvertime
            } else {
                workedTime.overtime += punch.duration
            }
        } else {
            workedTime.normal += punch.duration
        }

        this.incrementDailyHoursOf(punch)
        this.adjustWeeklyOvertime(workedTime)
    }

    private fun getDailyDurationOf(punch: Punch): Long = if (this.dailyDuration.containsKey(punch.day)) {
        this.dailyDuration[punch.day]!!
    } else {
        0L
    }

    private fun incrementDailyHoursOf(punch: Punch) {
        this.dailyDuration[punch.day] = this.getDailyDurationOf(punch) + punch.duration
    }

    private fun adjustWeeklyOvertime(workedTime: WorkedTime) {
        if (workedTime.normal > this.maximumWeeklyOvertime) {
            workedTime.overtime += workedTime.normal - this.maximumWeeklyOvertime
            workedTime.normal = this.maximumWeeklyOvertime
        }
    }

    fun getFrom(dateTime: LocalDateTime): WorkedTime { // FIXME: change parameter to DateTime
        val date = dateTime.toLocalDate().with(DayOfWeek.MONDAY)
        if (!this.workedTimes.containsKey(date)) {
            this.workedTimes[date] = WorkedTime()
        }
        return this.workedTimes[date]!!
    }

    class WorkedTime {
        var normal: Long = 0
        var overtime: Long = 0
    }
}
