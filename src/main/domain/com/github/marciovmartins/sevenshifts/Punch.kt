package com.github.marciovmartins.sevenshifts

import java.time.Duration
import java.time.LocalDateTime

class Punch(startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
    val day = startDateTime.toLocalDate()!!
    val duration = Duration.between(startDateTime, endDateTime).toMinutes()
}